package com.mzw.ctpmsbackend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductCreateDTO {
    private String title;
    private String description;
    private BigDecimal price;
    private String tags;
    private Integer stock;
    private Integer isVirtual;
    private Integer categoryId;

    private List<ProductImageDTO> images; // 图片信息
}
