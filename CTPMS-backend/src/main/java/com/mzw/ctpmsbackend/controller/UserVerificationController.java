package com.mzw.ctpmsbackend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mzw.ctpmsbackend.annotation.OperationLog;
import com.mzw.ctpmsbackend.common.utils.DataResult;
import com.mzw.ctpmsbackend.dto.UserVerificationDTO;
import com.mzw.ctpmsbackend.entity.UserVerification;
import com.mzw.ctpmsbackend.exception.ServiceException;
import com.mzw.ctpmsbackend.service.UserVerificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api("用户证件审核管理")
@RestController
@RequestMapping("/api/user-verifications")
@SaCheckLogin
public class UserVerificationController {

    @Resource
    private UserVerificationService userVerificationService;

    @ApiOperation("新增用户证件审核记录")
    @PostMapping
    @OperationLog(value = "新增用户证件认证记录", type = "USER_VERIFICATION")
    public DataResult<UserVerification> addVerification(@RequestBody @Validated UserVerificationDTO dto) {
        try {
            UserVerification verification = userVerificationService.addUserVerification(dto);
            return DataResult.success("审核记录创建成功", verification);
        } catch (ServiceException e) {
            return DataResult.fail(e.getMessage());
        }
    }

    @ApiOperation("删除用户证件审核记录")
    @DeleteMapping("/{id}")
    @SaCheckRole("admin")
    @OperationLog(value = "删除用户证件认证记录", type = "USER_VERIFICATION")
    public DataResult<Boolean> deleteVerification(@PathVariable Integer id) {
        try {
            boolean result = userVerificationService.deleteUserVerification(id);
            if (result) {
                return DataResult.success("删除成功",true);
            } else {
                return DataResult.fail("删除失败",false);
            }
        } catch (ServiceException e) {
            return DataResult.fail(e.getMessage());
        }
    }


    @ApiOperation("批量删除用户证件审核记录")
    @SaCheckRole("admin")
    @DeleteMapping("/batch")
    @OperationLog(value = "批量删除用户证件认证记录", type = "USER_VERIFICATION")
    public DataResult<Boolean> batchDeleteUserVerification(@RequestBody List<Integer> ids) {
        try {
            boolean result = userVerificationService.batchDeleteUserVerification(ids);
            if (result) {
                return DataResult.success("批量删除成功",true);
            } else {
                return DataResult.fail("批量删除失败",false);
            }
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    @ApiOperation("更新用户证件审核记录")
    @PutMapping
    @OperationLog(value = "更新用户证件审核记录", type = "USER_VERIFICATION")
    public DataResult<Boolean> updateVerification(@RequestBody @Validated UserVerificationDTO dto) {
        try {
            boolean result = userVerificationService.updateUserVerification(dto);
            if (result) {
                return DataResult.success("更新成功",true);
            } else {
                return DataResult.fail("更新失败",false);
            }
        } catch (ServiceException e) {
            return DataResult.fail(e.getMessage());
        }
    }

    @ApiOperation("分页获取所有用户证件审核记录")
    @GetMapping("/page")
    @SaCheckRole("admin")
    public DataResult<IPage<UserVerification>> getVerificationList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            IPage<UserVerification> result = userVerificationService.getUserVerificationList(page, size);
            return DataResult.success("获取所有用户证件审核记录",result);
        } catch (ServiceException e) {
            return DataResult.fail(e.getMessage());
        }
    }

    @ApiOperation("分页模糊搜索用户证件审核记录")
    @GetMapping("/search/{type}")
    @SaCheckRole("admin")
    public DataResult<IPage<UserVerification>> searchVerifications(
            @PathVariable("type") String type,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        try {
            IPage<UserVerification> result = userVerificationService.searchUserVerifications(page, size, keyword,type);
            return DataResult.success("搜索用户证件审核记录",result);
        } catch (ServiceException e) {
            return DataResult.fail(e.getMessage());
        }
    }

    @ApiOperation("获取所有用户证件待审核记录")
    @GetMapping
    @SaCheckRole("admin")
    public DataResult<List<UserVerification>> getAllPendingVerifications() {
        try {
            return DataResult.success("获取所有用户证件待审核记录",userVerificationService.getAllPendingUserVerifications());
        } catch (ServiceException e) {
            return DataResult.fail(e.getMessage());
        }
    }

    @ApiOperation("根据ID获取用户证件审核记录")
    @GetMapping("/{id}")
    public DataResult<UserVerification> getVerificationById(@PathVariable Integer id) {
        try {
            return DataResult.success("获取用户证件审核记录成功",userVerificationService.getUserVerificationById(id));
        } catch (ServiceException e) {
            return DataResult.fail(e.getMessage());
        }
    }

    @ApiOperation("审核通过")
    @PutMapping("/approve/{id}")
    @SaCheckRole("admin")
    public DataResult<Boolean> approveVerification(@PathVariable Integer id) {
        try {
            boolean result = userVerificationService.approveUserVerification(id);
            if (result) {
                return DataResult.success("审核通过成功",true);
            } else {
                return DataResult.fail("审核通过失败",false);
            }
        } catch (ServiceException e) {
            return DataResult.fail(e.getMessage());
        }
    }

    @ApiOperation("审核拒绝")
    @PutMapping("/reject/{id}")
    @SaCheckRole("admin")
    public DataResult<Boolean> rejectVerification(@PathVariable Integer id, @RequestParam String reason) {
        try {
            boolean result = userVerificationService.rejectUserVerification(id, reason);
            if (result) {
                return DataResult.success("审核拒绝成功",true);
            } else {
                return DataResult.fail("审核拒绝失败",false);
            }
        } catch (ServiceException e) {
            return DataResult.fail(e.getMessage());
        }
    }

    @ApiOperation("查询用户证件认证状态")
    @GetMapping("/status")
    public DataResult<Boolean> getVerification() {
        try {
            Integer userId = StpUtil.getLoginIdAsInt();
            boolean status = userVerificationService.getUserVerificationStatus(userId);

            if (status) {
                return DataResult.success("查询成功",true);
            } else {
                return DataResult.fail("查询失败",false);
            }
        } catch (ServiceException e) {
            return DataResult.fail(e.getMessage());
        }
    }
}

