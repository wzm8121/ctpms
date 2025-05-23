package com.mzw.ctpmsbackend.vo;

import com.mzw.ctpmsbackend.entity.ProductImage;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderItemVO {

    // 原有字段
    private Integer itemId;
    private String orderId;
    private Integer sellerId;
    private Integer productId;
    private Integer quantity;
    private BigDecimal itemPrice;//成交金额

    // 商品详情（来自 products 表）
    private String title;
    private String description;
    private Integer categoryId;
    private BigDecimal price;//商品金额
    private Integer stock;
    private Integer status;
    private Integer isVirtual;
    private Integer viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String tags;

    // 商品图片（来自 product_images 表）
    private List<ProductImage> images;
}
