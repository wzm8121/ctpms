package com.mzw.ctpmsbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzw.ctpmsbackend.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM users WHERE username = #{username} LIMIT 1")
    User selectByUsername(@Param("username") String username);

    @Select("SELECT * FROM users WHERE phone = #{phone}")
    User selectByPhone(String phone);

    @Select("SELECT * FROM users WHERE email = #{email}")
    User selectByEmail(String email);

    @Select("SELECT * FROM users WHERE student_id = #{studentId} AND password_hash = #{passwordHash}")
    User selectByStudentIdAndPassword(@Param("studentId") String studentId, @Param("passwordHash") String passwordHash);

    @Select("SELECT * FROM users WHERE student_id = #{studentId}")
    User selectByStudentId(@Param("studentId") String studentId);

}
