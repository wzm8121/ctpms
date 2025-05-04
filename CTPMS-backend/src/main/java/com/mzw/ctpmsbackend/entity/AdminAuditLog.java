package com.mzw.ctpmsbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("admin_audit_log")
public class AdminAuditLog {
    @TableId(type = IdType.AUTO)
    private Integer auditId;
    /**
     * 审核人ID
     */
    private Integer auditorId;

    /**
     * 审核人姓名
     */
    private String auditorName;

    private String operation;

    /**
     * 审核类型(USER/ITEM/ORDER)
     */
    private String auditType;

    /**
     * 目标ID
     */
    private Integer targetId;

    /**
     * 审核结果
     */
    private Integer result;//1为通过,2为不通过

    /**
     * 审核意见
     */
    private String reason;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 执行耗时(ms)
     */
    private Long time;


    /**
     * IP地址
     */
    private String ip;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
