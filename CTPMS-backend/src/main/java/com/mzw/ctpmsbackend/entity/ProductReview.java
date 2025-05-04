package com.mzw.ctpmsbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@TableName("product_review")
public class ProductReview {
    private Integer id;
    private Integer productId;
    private Integer status;       // 1=通过，2=失败
    private String type;   // 自动审核、人工审核
    private String reason;
    private String reviewBy;
    private LocalDateTime createdAt;
}
