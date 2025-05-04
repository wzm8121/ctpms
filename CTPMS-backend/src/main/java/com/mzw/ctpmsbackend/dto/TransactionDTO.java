package com.mzw.ctpmsbackend.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易数据传输对象
 */
@Data
public class TransactionDTO {
    /**
     * 交易编号
     */
    private String transactionNo;
    
    /**
     * 买家ID
     */
    private Integer buyerId;
    
    /**
     * 买家姓名
     */
    private String buyerName;
    
    /**
     * 卖家ID
     */
    private Integer sellerId;
    
    /**
     * 卖家姓名
     */
    private String sellerName;
    
    /**
     * 商品ID
     */
    private Integer itemId;
    
    /**
     * 商品名称
     */
    private String itemName;
    
    /**
     * 交易金额
     */
    private BigDecimal amount;
    
    /**
     * 交易状态
     * SUCCESS - 成功
     * FAILED - 失败
     * PENDING - 处理中
     * REFUNDED - 已退款
     */
    private String status;
    
    /**
     * 交易类型
     * TRADE - 普通交易
     * REFUND - 退款
     * TOPUP - 充值
     */
    private String type;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 交易备注
     */
    private String remark;
    
    // 可以根据需要添加其他字段
    
    /**
     * 支付方式
     */
    private String paymentMethod;
    
    /**
     * 物流信息
     */
    private String shippingInfo;
}