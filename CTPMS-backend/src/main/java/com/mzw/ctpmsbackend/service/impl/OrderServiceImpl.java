package com.mzw.ctpmsbackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mzw.ctpmsbackend.annotation.TransactionLog;
import com.mzw.ctpmsbackend.dto.OrderItemUpdateDTO;
import com.mzw.ctpmsbackend.dto.OrderUpdateDTO;
import com.mzw.ctpmsbackend.entity.*;
import com.mzw.ctpmsbackend.mapper.OrderItemMapper;
import com.mzw.ctpmsbackend.mapper.OrderMapper;
import com.mzw.ctpmsbackend.mapper.orderPayRecordMapper;
import com.mzw.ctpmsbackend.mapper.ProductImageMapper;
import com.mzw.ctpmsbackend.mapper.orderRefundRecordMapper;
import com.mzw.ctpmsbackend.mapper.ProductMapper;
import com.mzw.ctpmsbackend.service.OrderService;
import com.mzw.ctpmsbackend.service.ProductService;
import com.mzw.ctpmsbackend.mapper.orderTransferRecordMapper;
import com.mzw.ctpmsbackend.vo.OrderItemVO;
import com.mzw.ctpmsbackend.vo.OrderVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private ProductService productService;
    @Resource
    private OrderItemMapper orderItemMapper;
    @Resource
    private orderPayRecordMapper orderPayRecordMapper;
    @Resource
    private orderRefundRecordMapper orderRefundRecordMapper;
    @Resource
    private orderTransferRecordMapper orderTransferRecordMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private ProductImageMapper productImageMapper;

    @Override
    public Order getOrderById(String orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            return order;
        }
        return order;
    }


    private BigDecimal calculateTotalAmount(List<OrderItemUpdateDTO> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItemUpdateDTO item : items) {
            total = total.add(item.getItemPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        return total;
    }


    @Override
    @TransactionLog(value = "删除订单", type = "ORDER")
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteOrder(String orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        // 删除订单项
        orderItemMapper.delete(new LambdaQueryWrapper<OrderItem>()
                .eq(OrderItem::getOrderId, orderId));

        // 删除支付记录
        orderPayRecordMapper.delete(new LambdaQueryWrapper<OrderPayRecord>()
                .eq(OrderPayRecord::getOutTraceNo, order.getOrderId()));

        // 删除退款记录
        orderRefundRecordMapper.delete(new LambdaQueryWrapper<OrderRefundRecord>()
                .eq(OrderRefundRecord::getOutTraceNo, order.getOrderId()));

        // 删除转账记录
        orderTransferRecordMapper.delete(new LambdaQueryWrapper<OrderTransferRecord>()
                .eq(OrderTransferRecord::getOutTraceNo, order.getOrderId()));

        // 删除主订单
        int deleted = orderMapper.deleteById(orderId);
        return deleted > 0;
    }

    @Override
    @TransactionLog(value = "批量删除订单", type = "ORDER")
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteOrders(List<String> orderIds) {
        if (CollUtil.isEmpty(orderIds)) {
            throw new IllegalArgumentException("订单ID列表不能为空");
        }

        // 查询所有订单
        List<Order> orders = orderMapper.selectBatchIds(orderIds);
        if (orders.isEmpty()) {
            throw new RuntimeException("未找到任何要删除的订单");
        }

        // 提取 orderId 和 orderSn（即 outTraceNo）
        List<String> orderSns = orders.stream()
                .map(Order::getOrderId) // 假设 orderId 就是 out_trace_no
                .collect(Collectors.toList());

        // 删除关联数据
        orderItemMapper.delete(new LambdaQueryWrapper<OrderItem>()
                .in(OrderItem::getOrderId, orderIds));

        orderPayRecordMapper.delete(new LambdaQueryWrapper<OrderPayRecord>()
                .in(OrderPayRecord::getOutTraceNo, orderSns));

        orderRefundRecordMapper.delete(new LambdaQueryWrapper<OrderRefundRecord>()
                .in(OrderRefundRecord::getOutTraceNo, orderSns));

        orderTransferRecordMapper.delete(new LambdaQueryWrapper<OrderTransferRecord>()
                .in(OrderTransferRecord::getOutTraceNo, orderSns));

        // 删除订单
        int count = orderMapper.deleteBatchIds(orderIds);

        return count == orderIds.size();
    }


    @Override
    @TransactionLog(value = "修改订单", type = "ORDER")
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrder(OrderUpdateDTO dto) {
        Order order = orderMapper.selectById(dto.getOrderId());
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        for (OrderItemUpdateDTO itemDTO : dto.getItems()) {
            OrderItem item = orderItemMapper.selectById(itemDTO.getItemId());
            if (item != null && item.getOrderId().equals(dto.getOrderId())) {
                item.setQuantity(itemDTO.getQuantity());
                item.setPrice(itemDTO.getItemPrice());
                orderItemMapper.updateById(item);
            }
        }

        // 使用 BigDecimal 精确计算总金额
        BigDecimal totalAmount = calculateTotalAmount(dto.getItems());

        order.setDeliveryAddress(dto.getDeliveryAddress());
        order.setTotalAmount(totalAmount);
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateById(order);

        return true;
    }

    @Override
    public IPage<OrderVO> getOrderList(int page, int size, Long userId) {
        Page<Order> orderPage = new Page<>(page, size);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(Order::getUserId, userId);
        }
        wrapper.orderByDesc(Order::getCreatedAt);

        IPage<Order> rawPage = orderMapper.selectPage(orderPage, wrapper);
        return enrichOrders(rawPage); // 返回 IPage<OrderVO>
    }

    @Override
    public IPage<OrderVO> searchOrders(int page, int size, String keyword, Long userId) {
        Page<Order> orderPage = new Page<>(page, size);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(Order::getUserId, userId);
        }
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.and(w -> w.like(Order::getOrderId, keyword)
                    .or().like(Order::getDeliveryAddress, keyword));
        }
        wrapper.orderByDesc(Order::getCreatedAt);

        IPage<Order> rawPage = orderMapper.selectPage(orderPage, wrapper);
        return enrichOrders(rawPage); // 返回 IPage<OrderVO>
    }


    private IPage<OrderVO> enrichOrders(IPage<Order> ordersPage) {
        List<OrderVO> voList = ordersPage.getRecords().stream().map(order -> {
            OrderVO vo = BeanUtil.copyProperties(order, OrderVO.class);

            // 查询商品项
            List<OrderItemVO> items = orderItemMapper.selectList(
                    new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, order.getOrderId())
            ).stream().map(item -> {
                OrderItemVO itemVO = BeanUtil.copyProperties(item, OrderItemVO.class);

                // 查询商品详情
                Product product = productMapper.selectById(item.getProductId());
                if (product != null) {
                    itemVO.setTitle(product.getTitle());
                    itemVO.setDescription(product.getDescription());
                    itemVO.setCategoryId(product.getCategoryId());
                    itemVO.setPrice(product.getPrice());
                    itemVO.setStock(product.getStock());
                    itemVO.setStatus(product.getStatus());
                    itemVO.setIsVirtual(product.getIsVirtual());
                    itemVO.setViewCount(product.getViewCount());
                    itemVO.setCreatedAt(product.getCreatedAt());
                    itemVO.setUpdatedAt(product.getUpdatedAt());
                    itemVO.setTags(product.getTags());

                    // 查询商品图片
                    List<ProductImage> images = productImageMapper.selectList(
                            new LambdaQueryWrapper<ProductImage>().eq(ProductImage::getProductId, product.getProductId())
                    );
                    itemVO.setImages(images);
                }
                return itemVO;
            }).collect(Collectors.toList());
            vo.setItems(items);

            // 支付记录
            vo.setPayRecord(orderPayRecordMapper.selectOne(
                    new LambdaQueryWrapper<OrderPayRecord>().eq(OrderPayRecord::getOutTraceNo, order.getOrderId())
            ));
            // 退款记录
            vo.setRefundRecord(orderRefundRecordMapper.selectOne(
                    new LambdaQueryWrapper<OrderRefundRecord>().eq(OrderRefundRecord::getOutTraceNo, order.getOrderId())
            ));
            // 转账记录
            vo.setTransferRecord(orderTransferRecordMapper.selectOne(
                    new LambdaQueryWrapper<OrderTransferRecord>().eq(OrderTransferRecord::getOutTraceNo, order.getOrderId())
            ));

            return vo;
        }).collect(Collectors.toList());

        Page<OrderVO> result = new Page<>();
        result.setRecords(voList);
        result.setTotal(ordersPage.getTotal());
        result.setCurrent(ordersPage.getCurrent());
        result.setSize(ordersPage.getSize());
        return result;
    }


}
