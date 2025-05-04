package com.mzw.ctpmsbackend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderQueryDTO {
    private String orderId;
    private String userId;
    private Integer status;
    private String deliveryAddress;
    private Integer orderType; // 1小订单 2大订单
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private Integer pageNo = 1;
    private Integer pageSize = 10;
}
