package com.mzw.ctpmsbackend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemUpdateDTO {
    private Long itemId;
    private Integer quantity;
    private BigDecimal itemPrice;
}