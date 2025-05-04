package com.mzw.ctpmsbackend.vo;

import com.mzw.ctpmsbackend.entity.OrderPayRecord;
import com.mzw.ctpmsbackend.entity.OrderRefundRecord;
import com.mzw.ctpmsbackend.entity.OrderTransferRecord;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderVO {
    private String orderId;
    private String parentOrderId;
    private String userId;
    private Integer orderType;
    private BigDecimal totalAmount;
    private Integer status;
    private LocalDateTime paymentDeadline;
    private String deliveryAddress;
    private LocalDateTime createdAt;

    private List<OrderItemVO> items;
    private OrderPayRecord payRecord;
    private OrderRefundRecord refundRecord;
    private OrderTransferRecord transferRecord;
}