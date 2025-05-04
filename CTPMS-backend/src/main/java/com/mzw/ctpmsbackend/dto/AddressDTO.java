/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\dto\AddressDTO.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-22 13:05:45
 */
package com.mzw.ctpmsbackend.dto;

import lombok.Data;

/**
 * 地址数据传输对象
 */
@Data
public class AddressDTO {
    /**
     * 地址ID
     */
    private Integer addressId;

    /**
     * 关联用户ID
     */
    private Integer userId;

    /**
     * 名字
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 地址
     */
    private String address;

    /**
     * 地址状态：1-普通，2-默认
     */
    private Integer addressStatus;
}
