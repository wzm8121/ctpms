/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\service\OrderService.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-12 20:55:57
 */
package com.mzw.ctpmsbackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mzw.ctpmsbackend.common.utils.DataResult;
import com.mzw.ctpmsbackend.dto.OrderDTO;
import com.mzw.ctpmsbackend.dto.OrderUpdateDTO;
import com.mzw.ctpmsbackend.entity.Order;
import com.mzw.ctpmsbackend.vo.OrderVO;

import java.util.List;

/**
 * 订单服务接口
 * 提供订单的增删改查及搜索功能
 */
public interface OrderService {


    boolean updateOrder(OrderUpdateDTO orderUpdateDTO);

    boolean deleteOrder(String orderId);

    boolean deleteOrders(List<String> orderIds);



    Order getOrderById(String orderId);

    IPage<OrderVO> getOrderList(int page, int size, Long userId);

    IPage<OrderVO> searchOrders(int page, int size, String keyword, Long userId);

}
