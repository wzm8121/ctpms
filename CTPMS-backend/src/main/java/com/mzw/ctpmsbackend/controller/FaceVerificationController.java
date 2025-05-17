/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\controller\FaceVerificationController.java
 * @LastEditors: David Ma
 * @Description: 人脸验证管理
 * @Date: 2025-04-09 10:06:48
 */
package com.mzw.ctpmsbackend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mzw.ctpmsbackend.annotation.AuditLog;
import com.mzw.ctpmsbackend.annotation.OperationLog;
import com.mzw.ctpmsbackend.common.utils.DataResult;
import com.mzw.ctpmsbackend.dto.FaceVerificationDTO;
import com.mzw.ctpmsbackend.dto.UserVerificationDTO;
import com.mzw.ctpmsbackend.entity.FaceReview;
import com.mzw.ctpmsbackend.entity.UserVerification;
import com.mzw.ctpmsbackend.exception.ServiceException;
import com.mzw.ctpmsbackend.service.FaceVerificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;


@Api(tags = "人脸审核管理")
@SaCheckLogin
@RestController
@RequestMapping("/api/face-verifications")
public class FaceVerificationController {

    @Resource
    private FaceVerificationService faceVerificationService;

    @ApiOperation("创建人脸审核记录")
    @PostMapping
    @OperationLog(type = "FACE_VERIFICATION", value = "创建审核记录")
    public DataResult<FaceReview> createVerification(
            @RequestBody @Valid FaceVerificationDTO verificationDTO) {
        try {
            FaceReview verification = faceVerificationService.createVerification(verificationDTO);
            return DataResult.success("审核记录创建成功", verification);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    @ApiOperation("删除人脸审核记录")
    @DeleteMapping("/{id}")
    @SaCheckRole("admin")
    @OperationLog(value = "删除人脸审核记录", type = "FACE_VERIFICATION")
    public DataResult<Boolean> deleteFaceVerification(@PathVariable Integer id) {
        try {
            boolean result = faceVerificationService.deleteFaceVerification(id);
            if (result) {
                return DataResult.success("删除成功",true);
            } else {
                return DataResult.fail("删除失败",false);
            }
        } catch (ServiceException e) {
            return DataResult.fail(e.getMessage());
        }
    }

    @ApiOperation("批量删除人脸审核记录")
    @SaCheckRole("admin")
    @DeleteMapping("/batch")
    @OperationLog(value = "批量删除人脸审核记录", type = "FACE_VERIFICATION")
    public DataResult<Boolean> batchDeleteFaceVerification(@RequestBody List<Integer> ids) {
        try {
            boolean result = faceVerificationService.batchDeleteFaceVerification(ids);
            if (result) {
                return DataResult.success("批量删除成功",true);
            } else {
                return DataResult.fail("批量删除失败",false);
            }
        } catch (ServiceException e) {
            return DataResult.fail(e.getMessage());
        }
    }

    @ApiOperation("更新人脸审核记录")
    @PutMapping
    @SaCheckRole("admin")
    @OperationLog(value = "更新人脸审核记录", type = "FACE_VERIFICATION")
    public DataResult<Boolean> updateFaceVerification(@RequestBody @Validated FaceVerificationDTO dto) {
        try {
            boolean result = faceVerificationService.updateFaceVerification(dto);
            if (result) {
                return DataResult.success("更新成功",true);
            } else {
                return DataResult.fail("更新失败",false);
            }
        } catch (ServiceException e) {
            return DataResult.fail(e.getMessage());
        }
    }

    @ApiOperation("根据ID获取审核记录详情")
    @GetMapping("/{id}")
    @SaCheckRole("admin")
    public DataResult<FaceReview> getVerificationById(@PathVariable Integer id) {
        try {
            FaceReview verification = faceVerificationService.getVerificationById(id);
            return DataResult.success("获取审核记录详情成功",verification);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    @ApiOperation("获取所有待审核记录")
    @GetMapping("/pending")
    @SaCheckRole("admin")
    public DataResult<List<FaceReview>> getPendingVerifications() {
        try {
            List<FaceReview> verifications = faceVerificationService.getAllPendingVerifications();
            return DataResult.success("获取所有待审核记录成功",verifications);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    @ApiOperation("审核通过")
    @PutMapping("/approve/{verificationId}")
    @SaCheckRole("admin")
    public DataResult<Boolean> approveVerification(
            @PathVariable Integer verificationId) {
        try {
            boolean result = faceVerificationService.approveVerification(verificationId);
            if (result) {
                return DataResult.success("审核通过成功",true);
            } else {
                return DataResult.fail("审核通过失败",false);
            }
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    @ApiOperation("审核拒绝")
    @PutMapping("/reject/{verificationId}")
    @SaCheckRole("admin")
    public DataResult<Boolean> rejectVerification(
            @PathVariable Integer verificationId,
            @RequestParam String reason) {
        try {
            boolean result = faceVerificationService.rejectVerification(verificationId, reason);
            if (result) {
                return DataResult.success("审核拒绝成功",true);
            } else {
                return DataResult.fail("审核拒绝失败",false);
            }
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    @ApiOperation("分页获取审核记录")
    @GetMapping
    @SaCheckRole("admin")
    public DataResult<IPage<FaceReview>> getVerificationList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            IPage<FaceReview> verifications = faceVerificationService.getVerificationList(page, size);
            return DataResult.success("分页获取审核记录成功",verifications);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    @ApiOperation("搜索审核记录")
    @GetMapping("/search/{type}")
    @SaCheckRole("admin")
    public DataResult<IPage<FaceReview>> searchVerifications(
            @PathVariable("type") String type,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        try {
            IPage<FaceReview> verifications =
                    faceVerificationService.searchVerifications(page, size, keyword, type);
            return DataResult.success("搜索审核记录成功", verifications);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }


    @ApiOperation("查询用户人脸认证状态")
    @GetMapping("/status")
    public DataResult<Boolean> getVerificationsStatus() {
        try {
            Integer userId = StpUtil.getLoginIdAsInt();
            boolean status = faceVerificationService.getUserFaceVerificationStatus(userId);
            if (status) {
                return DataResult.success("查询成功",true);
            } else {
                return DataResult.fail("查询失败",false);
            }
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }


}
