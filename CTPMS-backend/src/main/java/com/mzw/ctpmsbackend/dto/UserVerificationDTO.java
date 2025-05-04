/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\dto\UserVerificationDTO.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-21 16:39:12
 */
package com.mzw.ctpmsbackend.dto;

import lombok.Data;

@Data
public class UserVerificationDTO {

    private Integer verificationId;
    private Integer studentId;
    private String idCardName;
    private String idCardNumber;

    private String studentCardFront;
    private String studentCardBack;
    private String idCardFront;
    private String idCardBack;

    private Integer status;
    private Integer verifiedBy;
    private String verifiedAt;
    private String reason;
}

