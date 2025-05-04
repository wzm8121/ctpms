package com.mzw.ctpmsbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("order_refund_record")
public class OrderRefundRecord implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("支付编号")
    private String traceNo;

    @ApiModelProperty("商户订单号")
    private String outTraceNo;

    @ApiModelProperty("退款编号")
    private String refundId;

    @ApiModelProperty("退款金额")
    private String refundAmount;

    @ApiModelProperty("退款原因")
    private String refundReason;

    @ApiModelProperty("退款状态")
    private String refundStatus;

    @ApiModelProperty("买家编号")
    private String buyerId;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("支付方式")
    private String payment;
}
