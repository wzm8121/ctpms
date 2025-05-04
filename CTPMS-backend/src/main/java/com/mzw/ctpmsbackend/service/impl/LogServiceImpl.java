package com.mzw.ctpmsbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.mzw.ctpmsbackend.entity.AdminAuditLog;
import com.mzw.ctpmsbackend.entity.TransactionLogEntity;
import com.mzw.ctpmsbackend.entity.UserOperationLog;
import com.mzw.ctpmsbackend.exception.ServiceException;
import com.mzw.ctpmsbackend.mapper.AdminAuditLogMapper;
import com.mzw.ctpmsbackend.mapper.TransactionLogMapper;
import com.mzw.ctpmsbackend.mapper.UserOperationLogMapper;
import com.mzw.ctpmsbackend.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogServiceImpl implements LogService {
    private final UserOperationLogMapper userOperationLogMapper;
    private final TransactionLogMapper transactionLogMapper;
    private final AdminAuditLogMapper adminAuditLogMapper;

    // ==================== 用户操作日志 ====================

    @Override
    public IPage<UserOperationLog> getUserOperationLogs(int page, int size) throws ServiceException {
        try {
            Page<UserOperationLog> pageParam = new Page<>(page, size);
            return userOperationLogMapper.selectPage(pageParam,
                    new QueryWrapper<UserOperationLog>().orderByDesc("create_time"));
        } catch (Exception e) {
            log.error("获取用户操作日志失败", e);
            throw new ServiceException("获取用户操作日志失败: " + e.getMessage());
        }
    }

    @Override
    public IPage<UserOperationLog> searchUserOperationLogs(int page, int size, String keyword) throws ServiceException {
        try {
            Page<UserOperationLog> pageParam = new Page<>(page, size);
            QueryWrapper<UserOperationLog> queryWrapper = new QueryWrapper<UserOperationLog>()
                    .orderByDesc("create_time");

            if (StringUtils.isNotBlank(keyword)) {
                queryWrapper.and(wrapper -> wrapper
                        .like("username", keyword)
                        .or().like("operation", keyword)
                        .or().like("method", keyword)
                        .or().like("params", keyword));
            }

            return userOperationLogMapper.selectPage(pageParam, queryWrapper);
        } catch (Exception e) {
            log.error("搜索用户操作日志失败", e);
            throw new ServiceException("搜索用户操作日志失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserOperationLog(Long id) throws ServiceException {
        if (id == null) {
            throw new ServiceException("ID不能为空");
        }
        try {
            int result = userOperationLogMapper.deleteById(id);
            if (result <= 0) {
                throw new ServiceException("删除失败，日志可能不存在");
            }
            log.info("删除用户操作日志成功，ID: {}", id);
        } catch (Exception e) {
            log.error("删除用户操作日志失败，ID: {}", id, e);
            throw new ServiceException("删除用户操作日志失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteUserOperationLog(List<Long> ids) throws ServiceException {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException("ID列表不能为空");
        }
        try {
            int result = userOperationLogMapper.deleteBatchIds(ids);
            if (result <= 0) {
                throw new ServiceException("删除失败，可能没有符合条件的日志");
            }
            log.info("批量删除用户操作日志成功，数量: {}", ids.size());
        } catch (Exception e) {
            log.error("批量删除用户操作日志失败", e);
            throw new ServiceException("批量删除用户操作日志失败: " + e.getMessage());
        }
    }

    @Override
    @Async
    public void saveUserOperationLog(UserOperationLog log1) {
        try {
            userOperationLogMapper.insert(log1);
        } catch (Exception e) {
            log.error("保存用户操作日志失败", e);
        }
    }

    // ==================== 交易日志 ====================

    @Override
    public IPage<TransactionLogEntity> getTransactionLogs(int page, int size) throws ServiceException {
        try {
            Page<TransactionLogEntity> pageParam = new Page<>(page, size);
            return transactionLogMapper.selectPage(pageParam,
                    new QueryWrapper<TransactionLogEntity>().orderByDesc("create_time"));
        } catch (Exception e) {
            log.error("获取交易日志失败", e);
            throw new ServiceException("获取交易日志失败: " + e.getMessage());
        }
    }

    @Override
    public IPage<TransactionLogEntity> searchTransactionLogs(int page, int size, String keyword) throws ServiceException {
        try {
            Page<TransactionLogEntity> pageParam = new Page<>(page, size);
            QueryWrapper<TransactionLogEntity> queryWrapper = new QueryWrapper<TransactionLogEntity>()
                    .orderByDesc("create_time");

            if (StringUtils.isNotBlank(keyword)) {
                queryWrapper.and(wrapper -> wrapper
                        .like("transaction_id", keyword)
                        .or().like("item_name", keyword)
                        .or().like("remark", keyword));
            }

            return transactionLogMapper.selectPage(pageParam, queryWrapper);
        } catch (Exception e) {
            log.error("搜索交易日志失败", e);
            throw new ServiceException("搜索交易日志失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTransactionLog(Long id) throws ServiceException {
        if (id == null) {
            throw new ServiceException("ID不能为空");
        }
        try {
            int result = transactionLogMapper.deleteById(id);
            if (result <= 0) {
                throw new ServiceException("删除失败，日志可能不存在");
            }
            log.info("删除交易日志成功，ID: {}", id);
        } catch (Exception e) {
            log.error("删除交易日志失败，ID: {}", id, e);
            throw new ServiceException("删除交易日志失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteTransactionLog(List<Long> ids) throws ServiceException {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException("ID列表不能为空");
        }
        try {
            int result = transactionLogMapper.deleteBatchIds(ids);
            if (result <= 0) {
                throw new ServiceException("删除失败，可能没有符合条件的日志");
            }
            log.info("批量删除交易日志成功，数量: {}", ids.size());
        } catch (Exception e) {
            log.error("批量删除交易日志失败", e);
            throw new ServiceException("批量删除交易日志失败: " + e.getMessage());
        }
    }

    @Override
    @Async
    public void saveTransactionLog(TransactionLogEntity log1) {
        try {
            transactionLogMapper.insert(log1);
        } catch (Exception e) {
            log.error("保存交易日志失败", e);
        }
    }

    // ==================== 管理员审核日志 ====================

    @Override
    public IPage<AdminAuditLog> getAdminAuditLogs(int page, int size) throws ServiceException {
        try {
            Page<AdminAuditLog> pageParam = new Page<>(page, size);
            return adminAuditLogMapper.selectPage(pageParam,
                    new QueryWrapper<AdminAuditLog>().orderByDesc("create_time"));
        } catch (Exception e) {
            log.error("获取管理员审核日志失败", e);
            throw new ServiceException("获取管理员审核日志失败: " + e.getMessage());
        }
    }

    @Override
    public IPage<AdminAuditLog> searchAdminAuditLogs(int page, int size, String keyword) throws ServiceException {
        try {
            Page<AdminAuditLog> pageParam = new Page<>(page, size);
            QueryWrapper<AdminAuditLog> queryWrapper = new QueryWrapper<AdminAuditLog>()
                    .orderByDesc("create_time");

            if (StringUtils.isNotBlank(keyword)) {
                queryWrapper.and(wrapper -> wrapper
                        .like("admin_name", keyword)
                        .or().like("action", keyword)
                        .or().like("target_type", keyword)
                        .or().like("remark", keyword));
            }

            return adminAuditLogMapper.selectPage(pageParam, queryWrapper);
        } catch (Exception e) {
            log.error("搜索管理员审核日志失败", e);
            throw new ServiceException("搜索管理员审核日志失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAdminAuditLog(Long id) throws ServiceException {
        if (id == null) {
            throw new ServiceException("ID不能为空");
        }
        try {
            int result = adminAuditLogMapper.deleteById(id);
            if (result <= 0) {
                throw new ServiceException("删除失败，日志可能不存在");
            }
            log.info("删除管理员审核日志成功，ID: {}", id);
        } catch (Exception e) {
            log.error("删除管理员审核日志失败，ID: {}", id, e);
            throw new ServiceException("删除管理员审核日志失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteAdminAuditLog(List<Long> ids) throws ServiceException {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException("ID列表不能为空");
        }
        try {
            int result = adminAuditLogMapper.deleteBatchIds(ids);
            if (result <= 0) {
                throw new ServiceException("删除失败，可能没有符合条件的日志");
            }
            log.info("批量删除管理员审核日志成功，数量: {}", ids.size());
        } catch (Exception e) {
            log.error("批量删除管理员审核日志失败", e);
            throw new ServiceException("批量删除管理员审核日志失败: " + e.getMessage());
        }
    }

    @Override
    @Async
    public void saveAdminAuditLog(AdminAuditLog log1) {
        try {
            adminAuditLogMapper.insert(log1);
        } catch (Exception e) {
            log.error("保存管理员审核日志失败", e);
        }
    }
}