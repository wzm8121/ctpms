/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\entity\FaceVerification.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-10 18:34:18
 */
package com.mzw.ctpmsbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("face_review")
public class FaceReview {
    @TableId(value = "face_review_id", type = IdType.AUTO)
    private Integer faceReviewId; // 主键ID
    
    private Integer userId; // 关联的用户ID
    private String faceImageUrl; // 人脸图片URL
    
    private Integer status; // 0-未审核, 1-审核通过, 2-审核拒绝
    private String reason; // 审核拒绝原因
    private Integer verifiedBy; // 审核管理员ID
    private LocalDateTime verifiedAt; // 审核时间

    
    private LocalDateTime createdAt; // 创建时间
}
