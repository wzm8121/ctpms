/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\service\ProductReviewService.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-02-17 14:20:38
 */
package com.mzw.ctpmsbackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mzw.ctpmsbackend.dto.ProductReviewDTO;
import com.mzw.ctpmsbackend.dto.ProductReviewMessage;
import com.mzw.ctpmsbackend.entity.ManualReviewRequest;
import com.mzw.ctpmsbackend.entity.ProductReview;
import com.mzw.ctpmsbackend.exception.ServiceException;
import org.springframework.stereotype.Service;


public interface ProductReviewService {
    void autoReview(ProductReviewMessage message) throws ServiceException;

    IPage<ProductReview> getReviewPage(int page, int size) throws ServiceException;

    IPage<ProductReview> searchReviewPage(int page, int size, String keyword, String type) throws ServiceException;

    ProductReview getReview(Integer reviewId) throws ServiceException;

    boolean deleteReview(Integer reviewId) throws ServiceException;

    void manualReview(ManualReviewRequest request) throws ServiceException;
}

