/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\service\AddressService.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-22 13:06:26
 */
package com.mzw.ctpmsbackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mzw.ctpmsbackend.dto.AddressDTO;

import com.mzw.ctpmsbackend.entity.Address;
import com.mzw.ctpmsbackend.exception.ServiceException;


/**
 * 地址服务接口
 */
public interface AddressService {
    /**
     * 添加地址
     * @return 添加成功的地址ID或其他标识
     */
    String addAddress(AddressDTO addressDTO) throws ServiceException;

    /**
     * 删除地址
     */
    void deleteAddress(Integer addressId) throws ServiceException;

    /**
     * 修改地址
     */
    void updateAddress(AddressDTO addressDTO) throws ServiceException;

    /**
     * 获取地址详情
     */
    Address getAddress(Integer addressId) throws ServiceException;

    /**
     * 获取用户地址列表
     */
    IPage<Address> getAddressListByUserId(int page, int size, Integer userId) throws ServiceException;

    /**
     * 搜索地址列表
     */
    IPage<Address> searchAddressList(int page, int size, Integer uid, String keyword) throws ServiceException;

    /**
     * 设置默认地址
     */
    void setDefaultAddress(Integer addressId) throws ServiceException;
}
