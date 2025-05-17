package com.mzw.ctpmsbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("transaction_record")

public class TransactionLogEntity {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String type;
    private BigDecimal amount;
    private String status;
    private String subject;
    private String description;
    private String fromAccount;
    private String toAccount;
    private String relatedId;
    private LocalDateTime createTime;        // 创建时间
}
