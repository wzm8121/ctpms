package com.mzw.ctpmsbackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mzw.ctpmsbackend.common.utils.DataResult;
import com.mzw.ctpmsbackend.entity.AdminAuditLog;
import com.mzw.ctpmsbackend.entity.TransactionLogEntity;
import com.mzw.ctpmsbackend.entity.UserOperationLog;
import com.mzw.ctpmsbackend.exception.ServiceException;

import java.util.List;

public interface LogService {
// ==================== 用户操作日志 ====================

    /**
     * 分页获取用户操作日志
     * @param page 页码（从1开始）
     * @param size 每页数量
     * @return 分页日志数据
     * @throws ServiceException 当查询失败时抛出
     */
    IPage<UserOperationLog> getUserOperationLogs(int page, int size) throws ServiceException;

    /**
     * 搜索用户操作日志
     * @param page 页码（从1开始）
     * @param size 每页数量
     * @param keyword 搜索关键词（可匹配用户名、操作类型等）
     * @return 分页搜索结果
     * @throws ServiceException 当查询失败时抛出
     */
    IPage<UserOperationLog> searchUserOperationLogs(int page, int size, String keyword, String searchType) throws ServiceException;

    /**
     * 删除单条用户操作日志
     * @param id 日志ID
     * @throws ServiceException 当删除失败时抛出
     */
    void deleteUserOperationLog(Long id) throws ServiceException;

    /**
     * 批量删除用户操作日志
     * @param ids 日志ID列表
     * @throws ServiceException 当删除失败时抛出
     */
    void batchDeleteUserOperationLog(List<Long> ids) throws ServiceException;

    /**
     * 保存用户操作日志（异步）
     * @param log 日志实体
     */
    void saveUserOperationLog(UserOperationLog log);

    // ==================== 交易日志 ====================

    /**
     * 分页获取交易日志
     * @param page 页码（从1开始）
     * @param size 每页数量
     * @return 分页日志数据
     * @throws ServiceException 当查询失败时抛出
     */
    IPage<TransactionLogEntity> getTransactionLogs(int page, int size) throws ServiceException;

    /**
     * 搜索交易日志
     * @param page 页码（从1开始）
     * @param size 每页数量
     * @param keyword 搜索关键词（可匹配交易号、用户ID等）
     * @return 分页搜索结果
     * @throws ServiceException 当查询失败时抛出
     */
    IPage<TransactionLogEntity> searchTransactionLogs(int page, int size, String keyword, String searchType) throws ServiceException;

    /**
     * 删除单条交易日志
     * @param id 日志ID
     * @throws ServiceException 当删除失败时抛出
     */
    void deleteTransactionLog(Long id) throws ServiceException;

    /**
     * 批量删除交易日志
     * @param ids 日志ID列表
     * @throws ServiceException 当删除失败时抛出
     */
    void batchDeleteTransactionLog(List<Long> ids) throws ServiceException;

    /**
     * 保存交易日志（异步）
     * @param log 日志实体
     */
    void saveTransactionLog(TransactionLogEntity log);

    // ==================== 管理员审核日志 ====================

    /**
     * 分页获取管理员审核日志
     * @param page 页码（从1开始）
     * @param size 每页数量
     * @return 分页日志数据
     * @throws ServiceException 当查询失败时抛出
     */
    IPage<AdminAuditLog> getAdminAuditLogs(int page, int size) throws ServiceException;

    /**
     * 搜索管理员审核日志
     * @param page 页码（从1开始）
     * @param size 每页数量
     * @param keyword 搜索关键词（可匹配管理员ID、操作类型等）
     * @return 分页搜索结果
     * @throws ServiceException 当查询失败时抛出
     */
    IPage<AdminAuditLog> searchAdminAuditLogs(int page, int size, String keyword, String searchType) throws ServiceException;

    /**
     * 删除单条管理员审核日志
     * @param id 日志ID
     * @throws ServiceException 当删除失败时抛出
     */
    void deleteAdminAuditLog(Long id) throws ServiceException;

    /**
     * 批量删除管理员审核日志
     * @param ids 日志ID列表
     * @throws ServiceException 当删除失败时抛出
     */
    void batchDeleteAdminAuditLog(List<Long> ids) throws ServiceException;

    /**
     * 保存管理员审核日志（异步）
     * @param log 日志实体
     */
    void saveAdminAuditLog(AdminAuditLog log);
}
