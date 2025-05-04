/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\entity\User.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-02-15 18:14:37
 */
package com.mzw.ctpmsbackend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@TableName("users")
public class User {
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;
    private String studentId;
    private String username;
    private String phone;
    private String email;
    private String passwordHash;
    private String realName;
    private Integer schoolId;
    private String avatarUrl;
    private Integer isSeller;
    private String role;
    private String salt;
    private Integer status;
    private Integer userReputation;
    private Integer faceVerified ;
    private Integer userVerified ;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
