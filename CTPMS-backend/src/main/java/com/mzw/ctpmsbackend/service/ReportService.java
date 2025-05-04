/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\service\ReportService.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-21 17:14:43
 */
package com.mzw.ctpmsbackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mzw.ctpmsbackend.dto.ReportDTO;
import com.mzw.ctpmsbackend.entity.Report;
import com.mzw.ctpmsbackend.exception.ServiceException;

/**
 * 投诉管理服务接口
 */
public interface ReportService {

    /**
     * 添加投诉
     *
     * @param reportDTO 投诉信息
     * @return 添加后的投诉信息
     * @throws ServiceException 操作失败时抛出
     */
    Report addReport(ReportDTO reportDTO) throws ServiceException;

    /**
     * 删除投诉
     *
     * @param id 投诉ID
     * @return 是否删除成功
     * @throws ServiceException 操作失败时抛出
     */
    boolean deleteReport(Integer id) throws ServiceException;

    /**
     * 更新投诉信息
     *
     * @param reportDTO 投诉信息
     * @return 更新后的投诉信息
     * @throws ServiceException 操作失败时抛出
     */
    Report updateReport(ReportDTO reportDTO) throws ServiceException;

    /**
     * 获取投诉详情
     *
     * @param id 投诉ID
     * @return 投诉信息
     * @throws ServiceException 操作失败时抛出
     */
    Report getReport(Integer id) throws ServiceException;

    /**
     * 分页获取投诉列表
     *
     * @param page 当前页码
     * @param size 每页数量
     * @return 分页投诉列表
     * @throws ServiceException 操作失败时抛出
     */
    IPage<Report> getReportList(int page, int size) throws ServiceException;

    /**
     * 搜索投诉
     *
     * @param page    当前页码
     * @param size    每页数量
     * @param keyword 搜索关键词
     * @return 符合搜索条件的投诉列表
     * @throws ServiceException 操作失败时抛出
     */
    IPage<Report> searchReports(int page, int size, String keyword) throws ServiceException;

    boolean approveReport(Integer reportId) throws ServiceException;

    boolean rejectReport(Integer reportId) throws ServiceException;
}

