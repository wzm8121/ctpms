package com.mzw.ctpmsbackend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mzw.ctpmsbackend.annotation.OperationLog;
import com.mzw.ctpmsbackend.common.utils.DataResult;
import com.mzw.ctpmsbackend.dto.ProductReviewDTO;
import com.mzw.ctpmsbackend.dto.ProductReviewMessage;
import com.mzw.ctpmsbackend.entity.Address;
import com.mzw.ctpmsbackend.entity.Category;
import com.mzw.ctpmsbackend.entity.ManualReviewRequest;
import com.mzw.ctpmsbackend.entity.ProductReview;
import com.mzw.ctpmsbackend.exception.ServiceException;
import com.mzw.ctpmsbackend.service.ProductReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/product/review")
@Api(tags = "商品审核管理")

public class ProductReviewController {

    @Autowired
    private ProductReviewService productReviewService;

    /**
     * 发起自动审核
     */
    @PostMapping("/auto")
    public DataResult<Void> autoReview(@RequestBody ProductReviewMessage message) {
        try {
            productReviewService.autoReview(message);
            return DataResult.success("商品自动审核已完成");
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    /**
     * 手动审核
     */
    @PostMapping("/manual")
    @SaCheckLogin
    public DataResult<String> manualReview(@RequestBody ManualReviewRequest request) {
        try {
            productReviewService.manualReview(request);
            return DataResult.success("手动审核完成");
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }
    /**
     * 分页获取商品审核列表
     * @param page 当前页码
     * @param size 每页数量
     * @return 分类列表
     */
    @SaCheckRole("admin")
    @GetMapping("/list")
    @ApiOperation("分页获取商品审核列表")
    @SaCheckLogin
    public DataResult<IPage<ProductReview>> getCategoryList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            IPage<ProductReview> result = productReviewService.getReviewPage(page, size);
            return DataResult.success(result);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    /**
     * 搜索商品审核列表
     * @param page 当前页码
     * @param size 每页数量
     * @param keyword 搜索关键字
     * @return 分类列表
     */
    @SaCheckRole("admin")
    @GetMapping("/search/{type}")
    @ApiOperation("搜索商品审核列表")
    @SaCheckLogin
    public DataResult<IPage<ProductReview>> searchCategories(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @PathVariable("type") String type) {
        try {
            IPage<ProductReview> result = productReviewService.searchReviewPage(page, size, keyword,type);
            return DataResult.success(result);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }

    /**
     * 获取审核详情
     * @param reviewId 审核Id
     * @return 审核详情
     */
    @GetMapping("/{reviewId}")
    @ApiOperation("获取审核详情")
    public DataResult<ProductReview> getProductReview(@PathVariable Integer reviewId) {
        try {
            ProductReview productReview = productReviewService.getReview(reviewId);
            return DataResult.success(productReview);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }


    /**
     * 删除审核记录（仅管理员）
     */
    @SaCheckRole("admin")
    @DeleteMapping("/delete/{reviewId}")
    @OperationLog(type = "USER", value = "删除审核记录")
    @ApiOperation("删除审核记录")
    @SaCheckLogin
    public DataResult<Boolean> deleteProductReview(@PathVariable Integer reviewId) {
        try {
            productReviewService.deleteReview(reviewId);
            return DataResult.success("审核记录删除成功",true);
        } catch (ServiceException e) {
            return DataResult.error(e.getMessage());
        }
    }


}
