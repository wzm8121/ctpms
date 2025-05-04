package com.mzw.ctpmsbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("transaction_log")

public class TransactionLogEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String transactionNo;    // 交易编号
    private Integer buyerId;           // 买家ID
    private String buyerName;       // 买家姓名
    private Integer sellerId;          // 卖家ID
    private String sellerName;      // 卖家姓名
    private Integer itemId;            // 商品ID
    private String itemName;        // 商品名称
    private BigDecimal amount;      // 交易金额
    private String type;            // 交易类型
    private String status;          // 交易状态(SUCCESS/FAILED/PENDING)
    private String operation;       // 操作描述
    private String params;          // 请求参数
    private String result;          // 返回结果
    private Long time;              // 执行耗时(ms)
    private String ip;              // 操作IP
    private LocalDateTime createTime;        // 创建时间
}
