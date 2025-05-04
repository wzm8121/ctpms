/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\dto\BlacklistDTO.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-13 15:11:00
 */
package com.mzw.ctpmsbackend.dto;

import lombok.Data;

/**
 * 黑名单数据传输对象
 * 包含黑名单的相关信息
 */
@Data
public class BlacklistDTO {
    private Integer targetId;
    private String reason;
}
