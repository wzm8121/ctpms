/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\dto\FaceRecordDTO.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-13 15:22:48
 */
package com.mzw.ctpmsbackend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class FaceRecordDTO {
    private Integer recordId;
    private Integer userId;
    private Integer result;
    private String imageUrl;
    private Integer deviceType;
    private Float confidence;
    private LocalDateTime createdAt;
}
