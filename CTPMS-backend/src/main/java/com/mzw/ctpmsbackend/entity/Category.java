/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\entity\Category.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-13 14:55:56
 */
package com.mzw.ctpmsbackend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@TableName("categories")
public class Category {
    @TableId(type = IdType.AUTO)
    private Integer categoryId;
    private String name;
    private Integer parentId;
    private Boolean isVirtual;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
