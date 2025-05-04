package com.mzw.ctpmsbackend.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderUpdateDTO {
    private String orderId;
    private String deliveryAddress;
    private List<OrderItemUpdateDTO> items;
}