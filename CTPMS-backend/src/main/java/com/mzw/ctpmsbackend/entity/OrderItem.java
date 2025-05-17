package com.mzw.ctpmsbackend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("order_items")
public class OrderItem {

    @TableId(value = "item_id", type = IdType.AUTO)
    private Integer itemId;

    @TableField("order_id")
    private String orderId;

    @TableField("seller_id")
    private Integer sellerId;

    @TableField("product_id")
    private Integer productId;

    @TableField("quantity")
    private Integer quantity;

    @TableField("price")
    private BigDecimal price;

}
