/*
 * @Author: David Ma
 * @FilePath: \campus-trading-platform-management-system\CTPMS-backend\src\main\java\com\mzw\ctpmsbackend\mapper\UserVerificationMapper.java
 * @LastEditors: David Ma
 * @Description: 该文件用于...
 * @Date: 2025-03-21 16:37:10
 */
package com.mzw.ctpmsbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mzw.ctpmsbackend.entity.UserVerification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserVerificationMapper extends BaseMapper<UserVerification> {
    IPage<UserVerification> searchVerifications(IPage<UserVerification> page, String keyword);

    @Select("SELECT * FROM user_verifications WHERE student_id = #{studentId}")
    UserVerification findByStudentId(@Param("studentId") Long studentId);
}
