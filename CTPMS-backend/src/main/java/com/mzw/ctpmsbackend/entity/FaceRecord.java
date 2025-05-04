/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\entity\FaceRecord.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-13 15:22:25
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
@TableName("face_records")
public class FaceRecord {
    @TableId(type = IdType.AUTO)
    private Integer recordId;
    private Integer userId;
    private Integer result;
    private String imageUrl;
    private Integer deviceType;
    private Float confidence;
    private LocalDateTime createdAt;
}
