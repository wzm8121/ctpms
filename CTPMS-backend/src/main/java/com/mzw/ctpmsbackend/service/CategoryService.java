/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\service\CategoryService.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-13 14:57:17
 */
package com.mzw.ctpmsbackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mzw.ctpmsbackend.common.utils.DataResult;
import com.mzw.ctpmsbackend.dto.CategoryDTO;
import com.mzw.ctpmsbackend.dto.CategoryTreeDTO;
import com.mzw.ctpmsbackend.entity.Category;
import com.mzw.ctpmsbackend.exception.ServiceException;

import java.util.List;

/**
 * 分类服务接口
 * 提供分类的增删改查及搜索功能
 */
public interface CategoryService extends IService<Category> {

    /**
     * 添加分类
     * @param categoryDTO 分类信息
     * @throws ServiceException 如果添加失败抛出业务异常
     */
    void addCategory(CategoryDTO categoryDTO) throws ServiceException;

    /**
     * 更新分类信息
     * @param categoryDTO 分类信息
     * @throws ServiceException 如果更新失败抛出业务异常
     */
    void updateCategory(CategoryDTO categoryDTO) throws ServiceException;

    /**
     * 删除分类（需确保无子分类）
     * @param categoryId 分类ID
     * @throws ServiceException 如果删除失败或存在子分类抛出业务异常
     */
    void deleteCategory(Integer categoryId) throws ServiceException;

    /**
     * 根据分类ID获取分类详情
     * @param categoryId 分类ID
     * @return 分类信息
     * @throws ServiceException 如果分类不存在或查询失败抛出业务异常
     */
    Category getCategoryById(Integer categoryId) throws ServiceException;

    /**
     * 获取分类列表（分页）
     * @param page 当前页码
     * @param size 每页数量
     * @return 分类分页结果
     * @throws ServiceException 如果查询失败抛出业务异常
     */
    IPage<Category> getCategoryList(int page, int size) throws ServiceException;

    /**
     * 通过关键词搜索分类（分页）
     * @param page 当前页码
     * @param size 每页数量
     * @param keyword 关键词（按分类名称模糊查询）
     * @return 分类分页结果
     * @throws ServiceException 如果搜索失败抛出业务异常
     */
    IPage<Category> searchCategories(int page, int size, String keyword,String type) throws ServiceException;

    /**
     * 获取分类树结构
     * @return 分类树
     * @throws ServiceException 如果构建树失败抛出业务异常
     */
    List<CategoryTreeDTO> getCategoryTree() throws ServiceException;
}

