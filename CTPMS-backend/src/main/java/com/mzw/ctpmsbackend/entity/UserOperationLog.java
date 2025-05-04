package com.mzw.ctpmsbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_operation_log")
public class UserOperationLog {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId = 100000;       // 用户ID
    private String username = "游客";   // 用户名
    private String operation;  // 操作描述
    private String type;       // 操作类型（如LOGIN, CREATE, DELETE等）
    private String method;     // 请求方法（如com.example.UserController.create()）
    private String params;     // 请求参数（JSON格式）
    private String result;     // 返回结果（JSON格式）
    private Long time;         // 执行耗时（毫秒）
    private String ip;         // 请求IP
    private LocalDateTime createTime;   // 操作时间
}
