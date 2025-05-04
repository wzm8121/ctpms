/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\dto\ReportDTO.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-21 17:14:17
 */
package com.mzw.ctpmsbackend.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReportDTO {
    private Integer reportId;
    private Integer reporterId;
    private Integer targetId;
    private String content;
    private List<String> evidenceUrls;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime handledAt;
    private Integer handledBy;
}
