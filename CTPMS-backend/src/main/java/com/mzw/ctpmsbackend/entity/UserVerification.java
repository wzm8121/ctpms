package com.mzw.ctpmsbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@TableName("user_verifications")
public class UserVerification {

    @TableId(value = "verification_id", type = IdType.AUTO)
    private Integer verificationId; // 主键ID

    private Integer studentId; // 关联的用户ID

    private String idCardName; // 身份证姓名
    private String idCardNumber; // 身份证号

    private String studentCardFront; // 学生证正面图片URL
    private String studentCardBack;  // 学生证背面图片URL
    private String idCardFront;      // 身份证正面图片URL
    private String idCardBack;       // 身份证背面图片URL

    private Integer status; // 0-未审核, 1-审核成功, 2-审核失败
    private Integer verifiedBy; // 审核管理员ID
    private LocalDateTime verifiedAt; // 审核时间
    private LocalDateTime createdAt;
    private String reason; // 审核失败原因
}

