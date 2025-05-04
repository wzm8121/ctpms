package com.mzw.ctpmsbackend.dto;

import lombok.Data;

@Data
public class AuditResultDTO {
    /**
     * 审核前状态
     */
    private String beforeStatus;
    
    /**
     * 审核后状态
     */
    private String afterStatus;
    
    /**
     * 审核意见
     */
    private String comment;
    
    /**
     * 目标ID
     */
    private Long targetId;
    
    /**
     * 目标类型
     */
    private String targetType;
}