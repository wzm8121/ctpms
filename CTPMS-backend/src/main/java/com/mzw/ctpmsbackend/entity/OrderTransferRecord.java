package com.mzw.ctpmsbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("order_transfer_record")
public class OrderTransferRecord implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("支付编号")
    private String traceNo;

    @ApiModelProperty("商户订单号")
    private String outTraceNo;

    @ApiModelProperty("转账金额")
    private String transAmount;

    @ApiModelProperty("订单标题")
    private String orderTitle;

    @ApiModelProperty("收款账号")
    private String payeeAccount;

    @ApiModelProperty("收款账号类型")
    private String payeeType;

    @ApiModelProperty("支付方式")
    private String payment;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("转账状态")
    private String transState;
}
