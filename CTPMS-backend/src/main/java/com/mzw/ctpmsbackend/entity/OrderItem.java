package com.mzw.ctpmsbackend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("order_items")
public class OrderItem {

    @TableId(value = "item_id", type = IdType.AUTO)
    private Long itemId;

    @TableField("order_id")
    private String orderId;

    @TableField("product_id")
    private Long productId;

    @TableField("quantity")
    private Integer quantity;

    @TableField("price")
    private BigDecimal price;

    @TableField("status")
    private Integer status;
}
