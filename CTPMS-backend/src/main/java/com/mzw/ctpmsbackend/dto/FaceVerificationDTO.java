/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\dto\FaceVerificationDTO.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-04-09 10:05:02
 */
package com.mzw.ctpmsbackend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FaceVerificationDTO {
    private Integer faceReviewId; // 主键ID

    private Integer userId; // 关联的用户ID
    private String faceImageUrl; // 人脸图片URL

    private Integer status; // 0-未审核, 1-审核通过, 2-审核拒绝
    private String reason; // 审核拒绝原因
    private Integer verifiedBy; // 审核管理员ID
    private LocalDateTime verifiedAt; // 审核时间


    private LocalDateTime createdAt; // 创建时间
}
