package com.mzw.ctpmsbackend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mzw.ctpmsbackend.annotation.OperationLog;
import com.mzw.ctpmsbackend.common.utils.DataResult;
import com.mzw.ctpmsbackend.dto.CategoryDTO;
import com.mzw.ctpmsbackend.dto.CategoryTreeDTO;
import com.mzw.ctpmsbackend.entity.Category;
import com.mzw.ctpmsbackend.exception.ServiceException;
import com.mzw.ctpmsbackend.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分类管理控制器
 * 包含分类的增删改查操作，使用MyBatisPlus进行分页查询
 * 所有请求都需要通过SaToken进行权限校验
 */
@Api(tags = "分类管理")
@RestController
@SaCheckLogin
@RequestMapping("/api/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 添加分类
     * @param categoryDTO 分类信息
     * @return 操作结果
     */
    @SaCheckRole("admin")
    @PostMapping("/add")
    @OperationLog(type = "CATEGORY", value = "添加分类")
    @ApiOperation("添加分类")
    public DataResult<String> addCategory(@RequestBody CategoryDTO categoryDTO) {
        try {
            categoryService.addCategory(categoryDTO);
            return DataResult.success("分类添加成功");
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    /**
     * 删除分类
     * @param categoryId 分类ID
     * @return 操作结果
     */
    @SaCheckRole("admin")
    @DeleteMapping("/delete/{categoryId}")
    @OperationLog(type = "CATEGORY", value = "删除分类")
    @ApiOperation("删除分类")
    public DataResult<String> deleteCategory(@PathVariable Integer categoryId) {
        try {
            categoryService.deleteCategory(categoryId);
            return DataResult.success("分类删除成功");
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    /**
     * 更新分类信息
     * @param categoryDTO 分类信息
     * @return 操作结果
     */
    @SaCheckRole("admin")
    @PutMapping("/update")
    @OperationLog(type = "CATEGORY", value = "更新分类")
    @ApiOperation("更新分类信息")
    public DataResult<String> updateCategory(@RequestBody CategoryDTO categoryDTO) {
        try {
            categoryService.updateCategory(categoryDTO);
            return DataResult.success("分类更新成功");
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    /**
     * 根据ID获取分类信息
     * @param categoryId 分类ID
     * @return 分类信息
     */
    @SaIgnore
    @GetMapping("/get/{categoryId}")
    @OperationLog(type = "CATEGORY", value = "获取分类详情")
    @ApiOperation("根据ID获取分类信息")
    public DataResult<Category> getCategoryById(@PathVariable Integer categoryId) {
        try {
            Category category = categoryService.getCategoryById(categoryId);
            return DataResult.success(category);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    /**
     * 分页获取分类列表
     * @param page 当前页码
     * @param size 每页数量
     * @return 分类列表
     */
    @SaIgnore
    @GetMapping("/list")
    @ApiOperation("分页获取分类列表")
    public DataResult<IPage<Category>> getCategoryList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            IPage<Category> result = categoryService.getCategoryList(page, size);
            return DataResult.success(result);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    /**
     * 搜索分类
     * @param page 当前页码
     * @param size 每页数量
     * @param keyword 搜索关键字
     * @return 分类列表
     */
    @SaIgnore
    @GetMapping("/search")
    @ApiOperation("搜索分类")
    public DataResult<IPage<Category>> searchCategories(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        try {
            IPage<Category> result = categoryService.searchCategories(page, size, keyword);
            return DataResult.success(result);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    /**
     * 获取分类树结构
     * @return 分类树
     */
    @SaIgnore
    @GetMapping("/tree")
    @ApiOperation("获取分类树结构")
    public DataResult<List<CategoryTreeDTO>> getCategoryTree() {
        try {
            List<CategoryTreeDTO> result = categoryService.getCategoryTree();
            return DataResult.success(result);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }
}
