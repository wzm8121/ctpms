package com.mzw.ctpmsbackend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("orders")
public class Order {

    @TableId(value = "order_id", type = IdType.INPUT)
    private String orderId;

    @TableField("parent_order_id")
    private String parentOrderId;

    @TableField("user_id")
    private Long userId;

    @TableField("order_type")
    private Integer orderType;

    @TableField("total_amount")
    private BigDecimal totalAmount;

    @TableField("status")
    private Integer status;

    @TableField("payment_deadline")
    private LocalDateTime paymentDeadline;

    @TableField("delivery_address")
    private String deliveryAddress;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    // 以下为非数据库字段，仅用于业务封装
    @TableField(exist = false)
    private List<Order> subOrders;

    @TableField(exist = false)
    private List<OrderItem> items;

    @TableField(exist = false)
    private List<OrderPayRecord> payRecords;

    @TableField(exist = false)
    private List<OrderRefundRecord> refundRecords;

    @TableField(exist = false)
    private List<OrderTransferRecord> transferRecords;
}
