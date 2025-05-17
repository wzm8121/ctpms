package com.mzw.ctpmsbackend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mzw.ctpmsbackend.annotation.OperationLog;
import com.mzw.ctpmsbackend.common.utils.DataResult;
import com.mzw.ctpmsbackend.entity.User;
import com.mzw.ctpmsbackend.exception.ServiceException;
import com.mzw.ctpmsbackend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 用户管理控制器
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/api/user")
@SaCheckLogin
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 添加用户（仅管理员）
     */
    @SaCheckRole("admin")
    @PostMapping("/add")
    @OperationLog(type = "USER", value = "添加用户")
    @ApiOperation("添加用户")
    public DataResult<Integer> addUser(@RequestBody @Valid User user) {
        try {
            Integer userId = userService.addUser(user);
            return DataResult.success("用户添加成功", userId);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    /**
     * 删除用户（仅管理员）
     */
    @SaCheckRole("admin")
    @DeleteMapping("/delete/{userId}")
    @OperationLog(type = "USER", value = "删除用户")
    @ApiOperation("删除用户")
    public DataResult<Void> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return DataResult.success("用户删除成功");
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/update")
    @OperationLog(type = "USER", value = "更新用户信息")
    @ApiOperation("更新用户信息")
    public DataResult<Void> updateUser(@RequestBody @Valid User user) {
        try {
            Integer loginUserId = StpUtil.getLoginIdAsInt();

            System.out.println(user);
            // 参数校验
            if (user.getUserId() == null) {
                return DataResult.error("用户ID不能为空");
            }

            // 权限校验
            if (!StpUtil.hasRole("admin") && !loginUserId.equals(user.getUserId())) {
                return DataResult.error("无权限修改其他用户信息");
            }

            userService.updateUser(user);
            return DataResult.success("用户信息更新成功");
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    /**
     * 根据ID获取用户信息
     */
    @SaIgnore
    @GetMapping("/info")
    public DataResult<User> getUserById(@RequestParam Integer userId) {
        try {
            User user = userService.getUserById(userId);
            return DataResult.success(user);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    /**
     * 分页获取用户列表（仅管理员）
     */
    @SaCheckRole("admin")
    @GetMapping("/list")
    @ApiOperation("分页获取用户列表")
    public DataResult<IPage<User>> getUserList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            IPage<User> result = userService.getUserList(page, size);
            return DataResult.success(result);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    /**
     * 用户搜索（仅管理员）
     */
    @SaCheckRole("admin")
    @GetMapping("/search/{type}")
    @ApiOperation("搜索用户")
    public DataResult<IPage<User>> searchUsers(
            @PathVariable("type") String type,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        try {
            IPage<User> result = userService.searchUsers(page, size, keyword,type);
            return DataResult.success(result);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    /**
     * 用户上传头像
     */
    @PostMapping("/uploadAvatar")
    @OperationLog(type = "USER", value = "上传头像")
    @ApiOperation("上传用户头像")
    public DataResult<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            Integer userId = StpUtil.getLoginIdAsInt();
            String avatarUrl = userService.uploadAvatar(file, userId);
            return DataResult.success("头像上传成功", avatarUrl);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }
}
