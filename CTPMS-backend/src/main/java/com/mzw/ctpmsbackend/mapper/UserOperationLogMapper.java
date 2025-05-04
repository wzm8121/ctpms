package com.mzw.ctpmsbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzw.ctpmsbackend.entity.UserOperationLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserOperationLogMapper extends BaseMapper<UserOperationLog> {
}