package com.mzw.ctpmsbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 商品实体类
 */
@Data
@TableName("products")
public class Product {
    @TableId(type = IdType.AUTO)
    private Integer productId; // 商品ID

    private Integer userId; // 用户学号

    private String title; // 商品标题

    private String description; // 商品描述

    private Long categoryId; // 分类ID

    private BigDecimal price; // 商品价格

    private Integer stock; // 商品库存

    private Integer status; // 商品状态：0-未审核，1-上架，2-已下架

    private Boolean isVirtual; // 是否为虚拟商品

    private Integer viewCount; // 浏览量

    private String Tags;//标签

    private LocalDateTime createdAt; // 创建时间

    private LocalDateTime updatedAt; // 更新时间
}
