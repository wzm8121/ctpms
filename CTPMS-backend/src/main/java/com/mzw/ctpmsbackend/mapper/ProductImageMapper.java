package com.mzw.ctpmsbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzw.ctpmsbackend.entity.ProductImage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductImageMapper extends BaseMapper<ProductImage> {

    /**
     * 批量插入商品图片
     */
    int batchInsert(@Param("list") List<ProductImage> images);
}