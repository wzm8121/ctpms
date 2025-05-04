/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\entity\Report.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-21 17:13:49
 */
package com.mzw.ctpmsbackend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@TableName("reports")
public class Report {
    @TableId
    private Integer reportId;
    private Integer reporterId;
    private Integer targetId;
    private String content;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> evidenceUrls;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime handledAt;
    private Integer handledBy;
}
