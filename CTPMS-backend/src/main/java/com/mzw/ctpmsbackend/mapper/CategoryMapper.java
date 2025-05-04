/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\mapper\CategoryMapper.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-13 14:56:54
 */
package com.mzw.ctpmsbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzw.ctpmsbackend.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
