/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\service\FaceVerificationService.java
 * @LastEditors: David Ma
 * @Description: 人脸验证服务接口
 * @Date: 2025-04-09 10:05:41
 */
package com.mzw.ctpmsbackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mzw.ctpmsbackend.dto.FaceVerificationDTO;
import com.mzw.ctpmsbackend.dto.UserVerificationDTO;
import com.mzw.ctpmsbackend.entity.FaceReview;
import com.mzw.ctpmsbackend.entity.UserVerification;
import com.mzw.ctpmsbackend.exception.ServiceException;

import java.util.List;

/**
 * 人脸核验服务接口
 * 提供人脸核验记录的创建、审批、查询等功能
 */
public interface FaceVerificationService {

    /**
     * 创建人脸核验记录
     * @param verificationDTO 核验请求数据传输对象
     * @return 创建成功的核验记录
     * @throws ServiceException 当参数无效或创建失败时抛出
     */
    FaceReview createVerification(FaceVerificationDTO verificationDTO) throws ServiceException;

    /**
     * 审批通过人脸核验记录
     * @param verificationId 核验记录ID
     * @throws ServiceException 当记录不存在、状态不合法或审批失败时抛出
     */
    boolean approveVerification(Integer verificationId) throws ServiceException;

    /**
     * 驳回人脸核验记录
     * @param verificationId 核验记录ID
     * @param reason 驳回信息（应包含驳回原因）
     * @throws ServiceException 当记录不存在、状态不合法或驳回失败时抛出
     */
    boolean rejectVerification(Integer verificationId, String reason) throws ServiceException;

    /**
     * 获取所有待处理的核验记录
     * @return 待处理核验记录列表（按创建时间升序）
     * @throws ServiceException 当查询失败时抛出
     */
    List<FaceReview> getAllPendingVerifications() throws ServiceException;

    /**
     * 根据ID获取核验记录详情
     * @param id 核验记录ID
     * @return 核验记录详情
     * @throws ServiceException 当记录不存在或查询失败时抛出
     */
    FaceReview getVerificationById(Integer id) throws ServiceException;

    /**
     * 分页获取核验记录列表
     * @param page 页码（从1开始）
     * @param size 每页数量
     * @return 分页核验记录
     * @throws ServiceException 当参数无效或查询失败时抛出
     */
    IPage<FaceReview> getVerificationList(int page, int size) throws ServiceException;

    /**
     * 搜索核验记录
     * @param page 页码（从1开始）
     * @param size 每页数量
     * @param keyword 搜索关键词（可匹配用户ID、姓名等字段）
     * @return 分页搜索结果
     * @throws ServiceException 当参数无效或查询失败时抛出
     */
    IPage<FaceReview> searchVerifications(int page, int size, String keyword) throws ServiceException;

    /**
     * 删除认证信息
     */
    boolean deleteFaceVerification(Integer verificationId) throws ServiceException;


    boolean batchDeleteFaceVerification(List<Integer> ids) throws ServiceException;

    /**
     * 更新认证信息
     */
    boolean updateFaceVerification(FaceVerificationDTO verificationDTO) throws ServiceException;

    boolean getUserFaceVerificationStatus(Integer userId) throws ServiceException;
}
