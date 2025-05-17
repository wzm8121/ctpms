package com.mzw.ctpmsbackend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mzw.ctpmsbackend.annotation.OperationLog;
import com.mzw.ctpmsbackend.common.utils.DataResult;
import com.mzw.ctpmsbackend.entity.AdminAuditLog;
import com.mzw.ctpmsbackend.entity.TransactionLogEntity;
import com.mzw.ctpmsbackend.entity.UserOperationLog;
import com.mzw.ctpmsbackend.exception.ServiceException;
import com.mzw.ctpmsbackend.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 日志管理控制器
 */
@Api(tags = "日志管理")
@RestController
@RequestMapping("/api/logs")
@SaCheckLogin
public class LogController {

    @Resource
    private LogService logService;

    // ==================== 用户操作日志 ====================

    @ApiOperation("分页查询用户操作日志")
    @GetMapping("/user-operations")
    public DataResult<IPage<UserOperationLog>> getUserOperationLogs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            IPage<UserOperationLog> result = logService.getUserOperationLogs(page, size);
            return DataResult.success(result);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    @ApiOperation("搜索用户操作日志")
    @GetMapping("/user-operations/search/{type}")
    public DataResult<IPage<UserOperationLog>> searchUserOperationLogs(
            @PathVariable("type") String type,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        try {
            IPage<UserOperationLog> result = logService.searchUserOperationLogs(page, size, keyword, type);
            return DataResult.success(result);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    @ApiOperation("删除单个用户操作日志")
    @SaCheckRole("admin")
    @DeleteMapping("/user-operations/{id}")
    @OperationLog(value = "删除用户操作日志", type = "USER_OPERATION")
    public DataResult<Void> deleteUserOperationLog(@PathVariable Long id) {
        try {
            logService.deleteUserOperationLog(id);
            return DataResult.success("删除成功");
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    @ApiOperation("批量删除用户操作日志")
    @SaCheckRole("admin")
    @DeleteMapping("/user-operations/batch")
    @OperationLog(value = "批量删除用户操作日志", type = "USER_OPERATION")
    public DataResult<Void> batchDeleteUserOperationLog(@RequestBody List<Long> ids) {
        try {
            logService.batchDeleteUserOperationLog(ids);
            return DataResult.success("批量删除成功");
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    // ==================== 交易日志 ====================

    @ApiOperation("分页查询交易日志")
    @GetMapping("/transactions")
    public DataResult<IPage<TransactionLogEntity>> getTransactionLogs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            IPage<TransactionLogEntity> result = logService.getTransactionLogs(page, size);
            return DataResult.success(result);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    @ApiOperation("搜索交易日志")
    @GetMapping("/transactions/search/{type}")
    public DataResult<IPage<TransactionLogEntity>> searchTransactionLogs(
            @PathVariable("type") String type,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        try {
            IPage<TransactionLogEntity> result = logService.searchTransactionLogs(page, size, keyword, type);
            return DataResult.success(result);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    @ApiOperation("删除单个交易日志")
    @SaCheckRole("admin")
    @DeleteMapping("/transactions/{id}")
    @OperationLog(value = "删除交易日志", type = "TRANSACTION")
    public DataResult<Void> deleteTransactionLog(@PathVariable Long id) {
        try {
            logService.deleteTransactionLog(id);
            return DataResult.success("删除成功");
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    @ApiOperation("批量删除交易日志")
    @SaCheckRole("admin")
    @DeleteMapping("/transactions/batch")
    @OperationLog(value = "批量删除交易日志", type = "TRANSACTION")
    public DataResult<Void> batchDeleteTransactionLog(@RequestBody List<Long> ids) {
        try {
            logService.batchDeleteTransactionLog(ids);
            return DataResult.success("批量删除成功");
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    // ==================== 管理员审核日志 ====================

    @ApiOperation("分页查询管理员审核日志")
    @GetMapping("/admin-audits")
    public DataResult<IPage<AdminAuditLog>> getAdminAuditLogs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            IPage<AdminAuditLog> result = logService.getAdminAuditLogs(page, size);
            return DataResult.success(result);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    @ApiOperation("搜索管理员审核日志")
    @GetMapping("/admin-audits/search/{type}")
    public DataResult<IPage<AdminAuditLog>> searchAdminAuditLogs(
            @PathVariable("type") String type,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        try {
            IPage<AdminAuditLog> result = logService.searchAdminAuditLogs(page, size, keyword, type);
            return DataResult.success(result);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    @ApiOperation("删除单个管理员审核日志")
    @SaCheckRole("admin")
    @DeleteMapping("/admin-audits/{id}")
    @OperationLog(value = "删除管理员审核日志", type = "ADMIN_AUDIT")
    public DataResult<Void> deleteAdminAuditLog(@PathVariable Long id) {
        try {
            logService.deleteAdminAuditLog(id);
            return DataResult.success("删除成功");
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    @ApiOperation("批量删除管理员审核日志")
    @SaCheckRole("admin")
    @DeleteMapping("/admin-audits/batch")
    @OperationLog(value = "批量删除管理员审核日志", type = "ADMIN_AUDIT")
    public DataResult<Void> batchDeleteAdminAuditLog(@RequestBody List<Long> ids) {
        try {
            logService.batchDeleteAdminAuditLog(ids);
            return DataResult.success("批量删除成功");
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }
}
