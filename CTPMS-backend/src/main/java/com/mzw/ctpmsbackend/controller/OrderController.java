package com.mzw.ctpmsbackend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mzw.ctpmsbackend.annotation.OperationLog;
import com.mzw.ctpmsbackend.common.utils.DataResult;
import com.mzw.ctpmsbackend.dto.OrderDTO;
import com.mzw.ctpmsbackend.dto.OrderUpdateDTO;
import com.mzw.ctpmsbackend.entity.Order;
import com.mzw.ctpmsbackend.service.OrderService;
import com.mzw.ctpmsbackend.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 订单管理控制器
 * 负责订单的增删改查，使用 MyBatisPlus 进行分页查询
 */
@Api(tags = "订单管理")
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Resource
    private OrderService orderService;
    

    /**
     * 删除订单（管理员可删除任何订单，普通用户只能删除自己的订单）
     *
     * @param orderId 订单 ID
     * @return 操作结果
     */
    @SaCheckLogin
    @DeleteMapping("/delete/{orderId}")
    @OperationLog(value = "删除订单", type = "ORDER")
    @ApiOperation("删除订单")
    public DataResult<String> deleteOrder(@PathVariable String orderId) {
        Long loginUserId = StpUtil.getLoginIdAsLong();

        // 获取订单信息
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            return DataResult.error("订单不存在");
        }

        // 只有管理员或订单所有者才能删除订单
        if (!StpUtil.hasRole("admin") && !loginUserId.equals(order.getUserId())) {
            return DataResult.error("无权限删除其他用户的订单");
        }

        boolean result = orderService.deleteOrder(orderId);
        return result ? DataResult.success("删除成功") : DataResult.fail("删除失败");
    }

    @SaCheckLogin
    @DeleteMapping("/delete/batch")
    @OperationLog(value = "批量删除订单", type = "ORDER")
    @ApiOperation("批量删除订单")
    public DataResult<String> deleteOrders(@RequestBody List<String> orderIds) {
        Long loginUserId = StpUtil.getLoginIdAsLong();

        if (CollUtil.isEmpty(orderIds)) {
            return DataResult.error("订单ID列表不能为空");
        }

        // 校验每一个订单是否存在且用户有权限删除
        for (String orderId : orderIds) {
            Order order = orderService.getOrderById(orderId);
            if (order == null) {
                return DataResult.error("订单不存在：" + orderId);
            }

            // 只有管理员或订单所有者才能删除订单
            if (!StpUtil.hasRole("admin") && !loginUserId.equals(order.getUserId())) {
                return DataResult.error("无权限删除订单：" + orderId);
            }
        }

        boolean result = orderService.deleteOrders(orderIds);
        return result ? DataResult.success("批量删除成功") : DataResult.fail("批量删除失败");
    }


    /**
     * 更新订单信息（管理员可修改任何订单，普通用户只能修改自己的订单）
     *
     * @param dto 订单信息
     * @return 操作结果
     */
    @SaCheckLogin
    @PutMapping("/update")
    @OperationLog(value = "更新订单信息", type = "ORDER")
    @ApiOperation("更新订单")
    public DataResult<Boolean> updateOrder(@RequestBody @Validated OrderUpdateDTO dto) {
        Long loginUserId = StpUtil.getLoginIdAsLong();

        // 获取订单信息
        Order existingOrder = orderService.getOrderById(dto.getOrderId());
        if (existingOrder == null) {
            return DataResult.error("订单不存在");
        }

        // 只有管理员或订单所有者才能修改订单
        if (!StpUtil.hasRole("admin") && !loginUserId.equals(existingOrder.getUserId())) {
            return DataResult.error("无权限修改其他用户的订单");
        }
        boolean result = orderService.updateOrder(dto);

        return DataResult.success(result);
    }

    /**
     * 获取订单详情（管理员可查询任何订单，普通用户只能查询自己的订单）
     *
     * @param orderId 订单 ID
     * @return 订单信息
     */
    @SaCheckLogin
    @GetMapping("/get/{orderId}")
    @ApiOperation("获取订单详情")
    public DataResult<Order> getOrderById(@PathVariable String orderId) {
        Long loginUserId = StpUtil.getLoginIdAsLong();

        // 获取订单信息
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            return DataResult.error("订单不存在");
        }

        // 只有管理员或订单所有者才能查看订单
        if (!StpUtil.hasRole("admin") && !loginUserId.equals(order.getUserId())) {
            return DataResult.error("无权限查看其他用户的订单");
        }

        return DataResult.success(order);
    }

    @SaCheckLogin
    @GetMapping("/list")
    @ApiOperation("分页获取订单列表")
    public DataResult<IPage<OrderVO>> getOrderList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        Integer loginUserId = StpUtil.getLoginIdAsInt();

        // 管理员查询所有订单，普通用户查询自己的订单
        IPage<OrderVO> orders;
        if (StpUtil.hasRole("admin")) {
            orders = orderService.getOrderList(page, size, null);
        } else {
            orders = orderService.getOrderList(page, size, loginUserId);
        }

        return DataResult.success(orders);
    }

    @SaCheckLogin
    @GetMapping("/search/{type}")
    @ApiOperation("搜索订单")
    public DataResult<IPage<OrderVO>> searchOrders(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @PathVariable("type") String type) {

        Integer loginUserId = StpUtil.getLoginIdAsInt();

        // 管理员可以搜索所有订单，普通用户只能搜索自己的订单
        IPage<OrderVO> orders;
        if (StpUtil.hasRole("admin")) {
            orders = orderService.searchOrders(page, size, keyword, type, null);
        } else {
            orders = orderService.searchOrders(page, size, keyword, type, loginUserId);
        }

        return DataResult.success(orders);
    }



}
