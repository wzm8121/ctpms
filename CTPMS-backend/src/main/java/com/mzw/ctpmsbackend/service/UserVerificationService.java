/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\service\UserVerificationService.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-21 16:35:07
 */
package com.mzw.ctpmsbackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mzw.ctpmsbackend.dto.UserVerificationDTO;
import com.mzw.ctpmsbackend.entity.UserVerification;
import com.mzw.ctpmsbackend.exception.ServiceException;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface UserVerificationService {

    /**
     * 添加用户认证信息
     */
    UserVerification addUserVerification(UserVerificationDTO verificationDTO) throws ServiceException;

    /**
     * 删除认证信息
     */
    boolean deleteUserVerification(Integer ids) throws ServiceException;


    boolean batchDeleteUserVerification(List<Integer> verificationIds) throws ServiceException;

    /**
     * 更新认证信息
     */
    boolean updateUserVerification(UserVerificationDTO verificationDTO) throws ServiceException;

    /**
     * 获取分页认证列表
     */
    IPage<UserVerification> getUserVerificationList(int page, int size) throws ServiceException;

    /**
     * 关键字搜索认证记录
     */
    IPage<UserVerification> searchUserVerifications(int page, int size, String keyword,String type) throws ServiceException;

    /**
     * 获取所有待审核的认证信息
     */
    List<UserVerification> getAllPendingUserVerifications() throws ServiceException;

    /**
     * 根据ID获取认证详情
     */
    UserVerification getUserVerificationById(Integer id) throws ServiceException;

    /**
     * 审核通过认证
     */
    boolean approveUserVerification(Integer verificationId) throws ServiceException;

    /**
     * 拒绝认证
     */
    boolean rejectUserVerification(Integer verificationId, String reason) throws ServiceException;

    /**
     * 拒绝认证
     */
    boolean getUserVerificationStatus(Integer userId) throws ServiceException;
}

