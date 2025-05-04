/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\mapper\AddressMapper.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-22 13:06:05
 */
package com.mzw.ctpmsbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzw.ctpmsbackend.entity.Address;
import org.apache.ibatis.annotations.Mapper;

/**
 * 地址Mapper接口
 */
@Mapper
public interface AddressMapper extends BaseMapper<Address> {
}
