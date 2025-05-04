package com.mzw.ctpmsbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.mzw.ctpmsbackend.entity.TransactionLogEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransactionLogMapper extends BaseMapper<TransactionLogEntity> {
}
