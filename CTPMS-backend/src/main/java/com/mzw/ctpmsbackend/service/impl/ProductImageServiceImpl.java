package com.mzw.ctpmsbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzw.ctpmsbackend.entity.ProductImage;
import com.mzw.ctpmsbackend.mapper.ProductImageMapper;
import com.mzw.ctpmsbackend.service.ProductImageService;
import org.nd4j.common.io.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImageServiceImpl extends ServiceImpl<ProductImageMapper, ProductImage>
        implements ProductImageService {


    @Override
    public void removeByProductId(Integer productId) {
        LambdaQueryWrapper<ProductImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductImage::getProductId, productId);
        this.remove(wrapper);
    }

}
