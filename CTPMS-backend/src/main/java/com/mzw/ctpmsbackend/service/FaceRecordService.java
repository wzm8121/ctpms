/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\service\FaceRecordService.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-13 15:23:46
 */
package com.mzw.ctpmsbackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mzw.ctpmsbackend.dto.FaceRecordDTO;
import com.mzw.ctpmsbackend.exception.ServiceException;

import java.util.List;
import java.util.Map;

/**
 * 人脸识别记录服务接口
 * 提供人脸识别记录的增删改查及统计功能
 */
public interface FaceRecordService {

    /**
     * 添加人脸识别记录
     * @param faceRecordDTO 人脸识别记录信息
     * @throws ServiceException 如果添加失败抛出业务异常
     */
    void addFaceRecord(FaceRecordDTO faceRecordDTO) throws ServiceException;

    /**
     * 根据用户ID获取人脸识别记录
     * @param userId 用户ID
     * @param page 当前页码
     * @param size 每页数量
     * @return 人脸识别记录分页列表
     * @throws ServiceException 如果查询失败抛出业务异常
     */
    IPage<FaceRecordDTO> getFaceRecordsByUser(Integer userId, int page, int size) throws ServiceException;

    /**
     * 获取人脸识别统计信息
     *
     * @throws ServiceException 如果统计失败抛出业务异常
     */
    List<Map<String, Object>> getFaceRecordStatistics() throws ServiceException;

    /**
     * 根据时间范围查询人脸记录
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param page 当前页码
     * @param size 每页数量
     * @return 人脸识别记录分页列表
     * @throws ServiceException 如果查询失败抛出业务异常
     */
    IPage<FaceRecordDTO> getFaceRecordsByTimeRange(String startTime, String endTime, int page, int size) throws ServiceException;

    /**
     * 删除人脸记录
     * @param id 记录ID
     * @throws ServiceException 如果删除失败抛出业务异常
     */
    void deleteFaceRecord(Integer id) throws ServiceException;

    /**
     * 搜索人脸记录
     * @param keyword 搜索关键字
     * @param page 当前页码
     * @param size 每页数量
     * @return 人脸识别记录分页列表
     * @throws ServiceException 如果搜索失败抛出业务异常
     */
    IPage<FaceRecordDTO> searchFaceRecords(int page, int size,String keyword, String searchType) throws ServiceException;
}
