/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\mapper\FaceVerificationMapper.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-04-09 10:05:21
 */
package com.mzw.ctpmsbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzw.ctpmsbackend.entity.FaceReview;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FaceVerificationMapper extends BaseMapper<FaceReview> {
    // 可根据需要添加自定义查询方法
}
