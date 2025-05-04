package com.mzw.ctpmsbackend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductCreateDTO {
    private String title;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Boolean isVirtual;
    // 其他商品字段...

    private List<ProductImageDTO> images; // 图片信息
}
