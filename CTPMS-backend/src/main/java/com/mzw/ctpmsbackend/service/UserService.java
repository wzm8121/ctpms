/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\service\UserService.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-02-15 18:13:14
 */
package com.mzw.ctpmsbackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mzw.ctpmsbackend.exception.ServiceException;
import org.springframework.web.multipart.MultipartFile;
import com.mzw.ctpmsbackend.common.utils.DataResult;
import com.mzw.ctpmsbackend.entity.User;
import org.springframework.stereotype.Service;

/**
 * 用户服务接口
 * 提供用户管理相关功能，包括增删改查、验证码发送、注册和头像上传
 */
public interface UserService {

    /**
     * 添加用户
     * @param user 用户信息
     * @return 添加成功的用户ID
     * @throws ServiceException 当添加失败或参数无效时抛出
     */
    Integer addUser(User user) throws ServiceException;

    /**
     * 删除用户
     * @param userId 用户ID
     * @throws ServiceException 当用户不存在或删除失败时抛出
     */
    void deleteUser(Long userId) throws ServiceException;

    /**
     * 更新用户信息
     * @param user 用户信息（需包含用户ID）
     * @throws ServiceException 当用户不存在或更新失败时抛出
     */
    void updateUser(User user) throws ServiceException;

    /**
     * 根据ID获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     * @throws ServiceException 当用户不存在或查询失败时抛出
     */
    User getUserById(Integer userId) throws ServiceException;

    /**
     * 分页获取用户列表
     * @param page 页码（从1开始）
     * @param size 每页数量
     * @return 分页用户列表
     * @throws ServiceException 当查询失败时抛出
     */
    IPage<User> getUserList(int page, int size) throws ServiceException;

    /**
     * 搜索用户
     * @param page 页码（从1开始）
     * @param size 每页数量
     * @param keyword 搜索关键词（可匹配用户名、邮箱等字段）
     * @return 分页搜索结果
     * @throws ServiceException 当查询失败时抛出
     */
    IPage<User> searchUsers(int page, int size, String keyword, String type) throws ServiceException;

    /**
     * 发送验证码到邮箱
     * @param email 邮箱地址
     * @throws ServiceException 当邮箱无效或发送失败时抛出
     */
    void sendVerificationCode(String email) throws ServiceException;

    /**
     * 用户注册
     * @param user 用户信息
     * @param code 验证码
     * @return 注册成功的用户ID
     * @throws ServiceException 当验证码错误或注册失败时抛出
     */
    Integer register(User user, String code) throws ServiceException;

    /**
     * 上传用户头像
     * @param file 头像文件
     * @param userId 用户ID
     * @return 头像URL
     * @throws ServiceException 当上传失败或用户不存在时抛出
     */
    String uploadAvatar(MultipartFile file, Integer userId) throws ServiceException;
}
