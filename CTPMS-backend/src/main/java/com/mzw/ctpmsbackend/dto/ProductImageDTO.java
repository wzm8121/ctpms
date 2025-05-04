package com.mzw.ctpmsbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductImageDTO {
    private Integer imageId;
    private String imageUrl;
    private Integer isMain;
}