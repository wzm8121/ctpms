package com.mzw.ctpmsbackend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mzw.ctpmsbackend.annotation.OperationLog;
import com.mzw.ctpmsbackend.common.utils.DataResult;
import com.mzw.ctpmsbackend.dto.ProductCreateDTO;
import com.mzw.ctpmsbackend.dto.ProductDTO;
import com.mzw.ctpmsbackend.entity.Product;
import com.mzw.ctpmsbackend.exception.ServiceException;
import com.mzw.ctpmsbackend.service.ProductService;
import com.mzw.ctpmsbackend.vo.ProductVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 商品管理控制器
 * 负责商品的增删改查
 */
@Api(tags = "商品管理")
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Resource
    private ProductService productService;


    /**
     * 添加商品（仅管理员或卖家）
     *
     * @param productDTO 商品数据传输对象
     * @return 添加后的商品信息
     */
    @ApiOperation("添加商品")
    @OperationLog(type = "PRODUCT", value = "添加商品")
    @SaCheckLogin
    @PostMapping
    public DataResult<Product> addProduct(@RequestBody ProductCreateDTO productDTO) {
        try {
            productService.createProduct(productDTO);
            return DataResult.success("商品添加成功");
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    /**
     * 删除商品（管理员可删除任何商品，普通用户只能删除自己的）
     *
     * @param id 商品ID
     * @return 删除结果
     */
    @ApiOperation("删除商品")
    @OperationLog(type = "PRODUCT", value = "删除商品")
    @SaCheckLogin
    @DeleteMapping("/{id}")
    public DataResult<Boolean> deleteProduct(@PathVariable Integer id) {
        try {
            Integer loginUserId = StpUtil.getLoginIdAsInt();
            ProductVO product = productService.getProduct(id);

            if (product == null) {
                return DataResult.error("商品不存在");
            }

            if (!StpUtil.hasRole("admin") && !loginUserId.equals(product.getUserId())) {
                return DataResult.error("无权限删除他人商品");
            }

            productService.deleteProduct(id);
            return DataResult.success("商品删除成功", true);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    /**
     * 更新商品信息（管理员可修改任何商品，普通用户只能修改自己的）
     *
     * @param productDTO 商品数据传输对象
     * @return 更新后的商品信息
     */
    @ApiOperation("更新商品")
    @OperationLog(type = "PRODUCT", value = "更新商品")
    @SaCheckLogin
    @PutMapping
    public DataResult<Product> updateProduct(@RequestBody ProductDTO productDTO) {
        try {
            Integer loginUserId = StpUtil.getLoginIdAsInt();
            ProductVO existingProduct = productService.getProduct(productDTO.getProductId());

            if (existingProduct == null) {
                return DataResult.error("商品不存在");
            }

            if (!StpUtil.hasRole("admin") && !loginUserId.equals(existingProduct.getUserId())) {
                return DataResult.error("无权限修改他人商品");
            }

            Product updated = productService.updateProduct(productDTO);
            return DataResult.success("商品更新成功", updated);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    /**
     * 获取商品详情
     *
     * @param id 商品ID
     * @return 商品信息
     */
    @ApiOperation("获取商品详情")
    @SaCheckLogin
    @GetMapping("/{id}")
    public DataResult<ProductVO> getProduct(@PathVariable Integer id) {
        try {
            ProductVO product = productService.getProduct(id);
            if (product == null) {
                return DataResult.error("商品不存在");
            }
            return DataResult.success(product);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    /**
     * 分页获取商品列表
     *
     * @param page 当前页码
     * @param size 每页数量
     * @return 商品分页列表
     */
    @ApiOperation("分页获取商品列表")
    @SaCheckLogin
    @GetMapping("/list")
    public DataResult<IPage<ProductVO>> getProductList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            return DataResult.success(productService.getProductList(page, size));
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    /**
     * 搜索商品
     *
     * @param page    当前页码
     * @param size    每页数量
     * @param keyword 搜索关键词（可选）
     * @return 搜索结果
     */
    @ApiOperation("搜索商品")
    @SaCheckLogin
    @GetMapping("/search")
    public DataResult<IPage<ProductVO>> searchProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        try {
            return DataResult.success(productService.searchProducts(page, size, keyword));
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }
}

