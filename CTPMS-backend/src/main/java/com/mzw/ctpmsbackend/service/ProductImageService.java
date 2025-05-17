package com.mzw.ctpmsbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mzw.ctpmsbackend.entity.ProductImage;

import java.util.List;

public interface ProductImageService extends IService<ProductImage> {
    void removeByProductId(Integer productId);

}
