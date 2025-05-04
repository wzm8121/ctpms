/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\dto\OrderItemDTO.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-12 20:54:36
 */
package com.mzw.ctpmsbackend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private Integer productId;
    private Integer quantity;
    private BigDecimal price;
}
