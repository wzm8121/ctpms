package com.mzw.ctpmsbackend.entity;

import lombok.Data;

@Data
public class ManualReviewRequest {
    private Integer id;           // 审核记录 ID
    private Integer status;       // 1=通过，2=失败
    private String reason;        //原因
}
