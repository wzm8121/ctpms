package com.mzw.ctpmsbackend.service.impl;

import java.time.LocalDateTime;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzw.ctpmsbackend.common.utils.DataResult;
import com.mzw.ctpmsbackend.dto.CategoryDTO;
import com.mzw.ctpmsbackend.dto.CategoryTreeDTO;
import com.mzw.ctpmsbackend.entity.Category;
import com.mzw.ctpmsbackend.exception.ServiceException;
import com.mzw.ctpmsbackend.mapper.CategoryMapper;
import com.mzw.ctpmsbackend.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 分类服务实现类
 * 提供分类的增删改查及搜索功能实现
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCategory(CategoryDTO categoryDTO) throws ServiceException {
        // 参数校验
        if (categoryDTO == null || !StringUtils.hasText(categoryDTO.getName())) {
            throw new ServiceException("分类名称不能为空");
        }

        try {
            Category category = new Category();
            BeanUtils.copyProperties(categoryDTO, category);
            category.setCreatedAt(LocalDateTime.now());
            category.setUpdatedAt(LocalDateTime.now());
            this.save(category);
        } catch (Exception e) {
            throw new ServiceException("分类添加失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategory(CategoryDTO categoryDTO) throws ServiceException {
        // 参数校验
        if (categoryDTO == null || categoryDTO.getCategoryId() == null) {
            throw new ServiceException("分类信息不能为空");
        }

        Category category = this.getById(categoryDTO.getCategoryId());
        if (category == null) {
            throw new ServiceException("分类不存在");
        }

        try {
            BeanUtils.copyProperties(categoryDTO, category);
            category.setUpdatedAt(LocalDateTime.now());
            this.updateById(category);
        } catch (Exception e) {
            throw new ServiceException("分类更新失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(Integer categoryId) throws ServiceException {
        if (categoryId == null) {
            throw new ServiceException("分类ID不能为空");
        }

        // 检查是否有子分类
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", categoryId);
        if (this.count(queryWrapper) > 0) {
            throw new ServiceException("请先删除子分类");
        }

        try {
            this.removeById(categoryId);
        } catch (Exception e) {
            throw new ServiceException("分类删除失败：" + e.getMessage());
        }
    }

    @Override
    public Category getCategoryById(Integer categoryId) throws ServiceException {
        if (categoryId == null) {
            throw new ServiceException("分类ID不能为空");
        }

        try {
            Category category = this.getById(categoryId);
            if (category == null) {
                throw new ServiceException("分类不存在");
            }
            return category;
        } catch (Exception e) {
            throw new ServiceException("获取分类失败：" + e.getMessage());
        }
    }

    @Override
    public IPage<Category> getCategoryList(int page, int size) throws ServiceException {
        try {
            Page<Category> pageParam = new Page<>(page, size);
            return this.page(pageParam);
        } catch (Exception e) {
            throw new ServiceException("获取分类列表失败：" + e.getMessage());
        }
    }

    @Override
    public IPage<Category> searchCategories(int page, int size, String keyword) throws ServiceException {
        try {
            Page<Category> pageParam = new Page<>(page, size);
            QueryWrapper<Category> queryWrapper = new QueryWrapper<>();

            if (StringUtils.hasText(keyword)) {
                queryWrapper.like("name", keyword);
            }
            return this.page(pageParam, queryWrapper);
        } catch (Exception e) {
            throw new ServiceException("搜索分类失败：" + e.getMessage());
        }
    }

    @Override
    public List<CategoryTreeDTO> getCategoryTree() throws ServiceException {
        try {
            List<Category> categories = this.list();
            return buildCategoryTree(categories);
        } catch (Exception e) {
            throw new ServiceException("获取分类树失败：" + e.getMessage());
        }
    }

    /**
     * 构建分类树结构
     * @param categories 所有分类列表
     * @return 分类树列表
     */
    private List<CategoryTreeDTO> buildCategoryTree(List<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            return Collections.emptyList();
        }

        // 创建映射表：categoryId -> CategoryTreeDTO
        Map<Integer, CategoryTreeDTO> categoryMap = new HashMap<>();
        List<CategoryTreeDTO> rootCategories = new ArrayList<>();

        // 先转换为 DTO 并存入 Map
        for (Category category : categories) {
            CategoryTreeDTO dto = new CategoryTreeDTO();
            dto.setCategoryId(category.getCategoryId());
            dto.setName(category.getName());
            dto.setParentId(category.getParentId());
            dto.setIsVirtual(category.getIsVirtual());
            dto.setChildren(new ArrayList<>()); // 初始化子分类列表
            categoryMap.put(dto.getCategoryId(), dto);
        }

        // 构建分类树
        for (CategoryTreeDTO dto : categoryMap.values()) {
            if (dto.getParentId() == null) {
                // 没有父分类的是根分类
                rootCategories.add(dto);
            } else {
                // 找到父分类，并添加到其 children 列表
                CategoryTreeDTO parent = categoryMap.get(dto.getParentId());
                if (parent != null) {
                    parent.getChildren().add(dto);
                }
            }
        }

        return rootCategories;
    }
}

