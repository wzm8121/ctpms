package com.mzw.ctpmsbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("order_pay_record")
public class OrderPayRecord implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("支付编号")
    private String traceNo;

    @ApiModelProperty("系统订单号")
    private String outTraceNo;

    @ApiModelProperty("支付金额")
    private String orderAmount;

    @ApiModelProperty("订单名称")
    private String orderSubject;

    @ApiModelProperty("支付方式 1. 支付宝")
    private String payment;

    @ApiModelProperty("支付状态")
    private String state;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("买方ID")
    private String buyerId;
}
