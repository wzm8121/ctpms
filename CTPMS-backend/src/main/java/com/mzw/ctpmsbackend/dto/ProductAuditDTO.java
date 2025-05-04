package com.mzw.ctpmsbackend.dto;

import lombok.Data;

@Data
public class ProductAuditDTO {
    private Long productId;
    private Integer status;        // 1=审核通过，-1=失败
    private Integer reviewType;    // 0=自动，1=人工
    private String remark;
    private String reviewer;
}
