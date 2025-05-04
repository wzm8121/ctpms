package com.mzw.ctpmsbackend.dto;

import lombok.Data;

@Data
public class ProductReviewDTO {
    private Integer productId;
    private Integer status;        // 1=通过, -1=失败
    private Integer type;    // 0=自动，1=人工
    private String remark;
    private String reviewer;
}
