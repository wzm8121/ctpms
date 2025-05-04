/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\mapper\OrderMapper.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-12 20:57:27
 */
package com.mzw.ctpmsbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzw.ctpmsbackend.entity.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
