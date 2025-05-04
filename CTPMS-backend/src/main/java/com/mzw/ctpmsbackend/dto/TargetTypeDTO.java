/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\dto\TargetTypeDTO.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-21 17:18:06
 */
package com.mzw.ctpmsbackend.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TargetTypeDTO {
    private Long id;

    @NotBlank(message = "类型名称不能为空")
    private String typeName;

    private String description;
}
