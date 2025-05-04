package com.mzw.ctpmsbackend.vo;

import lombok.Data;

@Data
public class ProductImageVO {
    private Integer imageId;
    private String imageUrl;
    private Boolean isMain;
}

