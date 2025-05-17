package com.mzw.ctpmsbackend.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductVO {
    private Integer productId; // 商品ID

    private Integer userId; // 用户学号

    private String title; // 商品标题

    private String description; // 商品描述

    private Integer categoryId; // 分类ID

    private BigDecimal price; // 商品价格

    private Integer stock; // 商品库存

    private Integer status; // 商品状态：0-未审核，1-上架，2-已下架

    private Integer isVirtual; // 是否为虚拟商品

    private Integer viewCount; // 浏览量

    private String Tags;//标签

    private LocalDateTime createdAt; // 创建时间

    private LocalDateTime updatedAt; // 更新时间
    private List<ProductImageVO> images;
    // 可根据实际情况添加更多字段
}
