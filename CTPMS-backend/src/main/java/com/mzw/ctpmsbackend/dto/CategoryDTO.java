/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\dto\CategoryDTO.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-13 14:56:22
 */
package com.mzw.ctpmsbackend.dto;

import lombok.Data;

@Data
public class CategoryDTO {
    private Integer categoryId;
    private String name;
    private Integer parentId;
    private Boolean isVirtual;
}
