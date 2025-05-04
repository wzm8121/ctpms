package com.mzw.ctpmsbackend.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.mzw.ctpmsbackend.annotation.OperationLog;
import com.mzw.ctpmsbackend.common.utils.EncryptUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.mzw.ctpmsbackend.common.utils.CacheClient;
import com.mzw.ctpmsbackend.common.utils.DataResult;
import com.mzw.ctpmsbackend.dto.LoginRequest;
import com.mzw.ctpmsbackend.dto.UserDTO;
import com.mzw.ctpmsbackend.entity.User;
import com.mzw.ctpmsbackend.mapper.UserMapper;
import com.mzw.ctpmsbackend.service.UserService;
import com.wf.captcha.SpecCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Api(tags = "用户登录")
@Slf4j
@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class LoginController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CacheClient cacheClient;

    @Value("${mzw.password-key}")
    private String passwordKey;

    private static final String VERIFICATION_CODE_PREFIX = "login_code:";
    @Autowired
    private UserService userService;

    @ApiOperation("一键登录（通过 userId，开发专用）")
    @PostMapping("/dev-login")
    public DataResult<HashMap<String, Object>> devLogin(@RequestParam Integer userId) {
        // 直接根据 userId 查询
        User loginUser = userMapper.selectById(userId);
        if (loginUser == null) {
            return DataResult.error("用户不存在");
        }

        if (loginUser.getStatus() == 0) {
            return DataResult.error("账号无权限登录");
        }

        // 使用 SaToken 登录，不验证密码
        StpUtil.login(loginUser.getUserId());

        // 获取当前会话的 Session
        SaSession session = StpUtil.getSession();
        session.set("studentId", loginUser.getStudentId());
        session.set("userId", loginUser.getUserId());
        session.set("username", loginUser.getUsername());
        session.set("email", loginUser.getEmail());
        session.set("role", loginUser.getRole());
        session.set("avatarUrl", loginUser.getAvatarUrl());


        UserDTO userDTO = BeanUtil.copyProperties(loginUser, UserDTO.class);
        HashMap<String, Object> result = new HashMap<>();
        result.put("user", userDTO);
        result.put("token", StpUtil.getTokenInfo());

        //更新登录时间
        loginUser.setUpdatedAt(LocalDateTime.now());

        userMapper.updateById(loginUser);

        return new DataResult<>(200, "一键登录成功", result);
    }



    @ApiOperation("获取验证码 key")
    @GetMapping("/getVCodeKey")
    public DataResult<String> getCaptchaKey(HttpServletRequest request) {
        String ip = request.getRemoteAddr();


        // 生成唯一 captchaKey（IP + 时间戳）
        String captchaKey = ip + "_" + System.currentTimeMillis();
        log.info("生成的验证码 key: {}", captchaKey);

        return DataResult.success("验证码 key 生成成功", captchaKey);
    }

    @ApiOperation("获取图片验证码")
    @GetMapping("/getVCode")
    public void getCode(@RequestParam String captchaKey, HttpServletResponse response) {
        log.info("获取验证码，key: {}", captchaKey);

        // 使用 EasyCaptcha 生成验证码
        SpecCaptcha specCaptcha = new SpecCaptcha(120, 45, 4);
        String code = specCaptcha.text();  // 获取验证码字符
        log.info("生成的验证码 content: {}", code);

        // 存入 Redis，过期时间 5 分钟
        cacheClient.set(VERIFICATION_CODE_PREFIX + captchaKey, code, 5L, TimeUnit.MINUTES);
        log.info("验证码存入 Redis，key: {}", VERIFICATION_CODE_PREFIX + captchaKey);

        // 设置响应类型为图片
        response.setContentType("image/jpeg");
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            specCaptcha.out(outputStream);  // 输出验证码图片
        } catch (IOException e) {
            log.error("生成验证码失败", e);
            throw new RuntimeException("生成验证码失败");
        }
    }

    @ApiOperation("校验图片验证码")
    @PostMapping("/check/code/{vcode}")
    public DataResult<String> checkVcode(
            @PathVariable String vcode,
            @RequestParam String captchaKey) {
        // 从 Redis 获取验证码
        String redisKey = VERIFICATION_CODE_PREFIX + captchaKey;
        String code = cacheClient.get(redisKey); // 确保不使用 JSON 解析
        log.info("获取 Redis 中的验证码，key: {}, 取到的值: {}", redisKey, code);

        if (StrUtil.isBlank(code)) {
            return DataResult.error("验证码已过期，请刷新重试");
        }
        if (!StrUtil.equalsIgnoreCase(vcode, code)) {
            return DataResult.error("验证码错误，请重新输入");
        }

        // 校验成功后删除 Redis 中的验证码
        cacheClient.delete(redisKey);
        log.info("验证码校验通过，已删除 Redis 中的验证码");

        return DataResult.success("验证码正确");
    }

    @ApiOperation("登录（用户名 + 密码 + 验证码）")
    @OperationLog(value = "用户登录", type = "USER")
    @PostMapping("/login")
    public DataResult<Map<String, Object>> doLogin(
            @RequestBody LoginRequest loginRequest,
            @RequestParam String captchaKey) {

        String vcode = loginRequest.getVcode();
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        // 校验验证码
        DataResult<String> checkResult = checkVcode(vcode, captchaKey);
        if (checkResult.getCode() != 200) {
            return DataResult.error(checkResult.getMessage());
        }

        // 根据 username 查找用户
        User loginUser = userMapper.selectByUsername(username);
        System.out.println(loginUser);
        if (loginUser == null) {
            return DataResult.error("未找到用户");
        }

        if (loginUser.getStatus() == 0) {
            return DataResult.error("账号无权限登录");
        }

        String salt = loginUser.getSalt();
        if (salt == null || salt.isEmpty()) {
            return DataResult.error("用户信息异常，请联系管理员");
        }

        String encryptedPassword = EncryptUtil.encrypt(salt, password);

        System.out.println(encryptedPassword);
        if (!encryptedPassword.equals(loginUser.getPasswordHash())) {
            return DataResult.error("用户名或密码错误");
        }

        // 登录成功，使用 SaToken
        StpUtil.login(loginUser.getUserId());

        // 设置 session
        SaSession session = StpUtil.getSession();
        session.set("userId", loginUser.getUserId());
        session.set("username", loginUser.getUsername());
        session.set("role", loginUser.getRole());
        session.set("email", loginUser.getEmail());

        // 返回 DTO + token
        UserDTO userDTO = BeanUtil.copyProperties(loginUser, UserDTO.class);
        Map<String, Object> result = new HashMap<>();
        result.put("user", userDTO);
        result.put("token", StpUtil.getTokenInfo());

        // 更新登录时间
        loginUser.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(loginUser);

        return DataResult.success("登录成功", result);
    }



    /**
     * 用户登出
     *
     * @return 登出结果
     */
    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public DataResult<String> logout() {
        StpUtil.logout();
        return DataResult.success("退出登录成功");
    }

    /**
     * 检查登录状态
     *
     * @return 登录状态
     */
    @ApiOperation("验证是否登录")
    @GetMapping("/is-login")
    public DataResult<String> isLogin() {
        boolean isLogin = StpUtil.isLogin();
        return DataResult.success(isLogin ? "用户已登录" : "用户未登录");
    }
}
