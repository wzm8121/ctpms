package com.mzw.ctpmsbackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mzw.ctpmsbackend.common.utils.DataResult;
import com.mzw.ctpmsbackend.dto.BlacklistDTO;
import com.mzw.ctpmsbackend.entity.Blacklist;
import com.mzw.ctpmsbackend.exception.ServiceException;

/**
 * 黑名单服务接口
 * 提供黑名单的增删改查及搜索功能
 */
public interface BlacklistService {

    /**
     * 添加记录到黑名单
     * @param blacklistDTO 黑名单记录DTO
     * @throws ServiceException 如果添加失败抛出业务异常
     */
    void addToBlacklist(BlacklistDTO blacklistDTO) throws ServiceException;

    /**
     * 从黑名单移除记录
     * @param recordId 黑名单记录ID
     * @throws ServiceException 如果移除失败抛出业务异常
     */
    void removeFromBlacklist(Integer recordId) throws ServiceException;

    /**
     * 获取黑名单分页列表
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     * @throws ServiceException 如果查询失败抛出业务异常
     */
    IPage<Blacklist> getBlacklist(int page, int size) throws ServiceException;

    /**
     * 检查目标是否在黑名单中
     * @param targetId 目标ID
     * @return true-在黑名单中，false-不在黑名单中
     * @throws ServiceException 如果查询失败抛出业务异常
     */
    boolean isInBlacklist(Integer targetId) throws ServiceException;

    /**
     * 搜索黑名单记录
     * @param page 页码
     * @param size 每页大小
     * @param keyword 搜索关键词
     * @return 分页结果
     * @throws ServiceException 如果搜索失败抛出业务异常
     */
    IPage<Blacklist> searchBlacklist(int page, int size, String keyword) throws ServiceException;
}
