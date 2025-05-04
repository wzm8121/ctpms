package com.mzw.ctpmsbackend.controller;

import com.mzw.ctpmsbackend.annotation.OperationLog;
import com.mzw.ctpmsbackend.common.utils.DataResult;
import com.mzw.ctpmsbackend.dto.RegisterDTO;
import com.mzw.ctpmsbackend.entity.User;
import com.mzw.ctpmsbackend.exception.ServiceException;
import com.mzw.ctpmsbackend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Email;

/**
 * 用户注册控制器
 * 包含发送验证码和用户注册功能
 */
@Api(tags = "用户注册")
@RestController
@RequestMapping("/api/register")
public class RegisterController {

    @Resource
    private UserService userService;

    /**
     * 发送验证码
     *
     * @param email 用户邮箱
     * @return 操作结果
     */
    @ApiOperation("发送验证码")
    @PostMapping("/send-code")
    @OperationLog(type = "REGISTER", value = "发送验证码")
    public DataResult<Void> sendVerificationCode(@RequestParam @Email String email) {
        try {
            userService.sendVerificationCode(email);
            return DataResult.success("验证码已发送");
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    /**
     * 用户注册
     *
     * @param registerDTO 注册信息（包含用户信息和验证码）
     * @return 注册结果
     */
    @ApiOperation("用户注册")
    @PostMapping
    @OperationLog(type = "REGISTER", value = "用户注册")
    public DataResult<Integer> register(@RequestBody @Valid RegisterDTO registerDTO) {
        try {
            Integer userId = userService.register(registerDTO.getUser(), registerDTO.getCode());
            return DataResult.success("注册成功", userId);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }
}
