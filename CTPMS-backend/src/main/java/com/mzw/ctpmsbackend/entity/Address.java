/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\entity\Address.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-22 13:05:21
 */
package com.mzw.ctpmsbackend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 地址实体类
 */
@Data
@TableName("address")
public class Address implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 地址ID
     */
    @TableId
    private Integer addressId;

    /**
     * 关联用户ID
     */
    private Integer UserId;

    /**
     * 地址
     */
    private String address;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 地址状态：1-普通，2-默认
     */
    private Integer addressStatus;
}
