package com.mzw.ctpmsbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("product_images")
public class ProductImage {

    @TableId(type = IdType.AUTO)
    private Integer imageId;

    private Integer productId;

    private String imageUrl;

    private Integer isMain;
}
