/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\dto\OrderDTO.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-12 20:54:08
 */
package com.mzw.ctpmsbackend.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderDTO {
    private String orderId;
    private Integer userId;
    private List<OrderItemDTO> items;
    private String deliveryAddress;
    private String parentOrderId;
}
