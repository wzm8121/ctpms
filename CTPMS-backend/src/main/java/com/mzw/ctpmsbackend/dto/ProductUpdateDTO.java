package com.mzw.ctpmsbackend.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductUpdateDTO {
    @NotNull
    private Long productId;

    private String title;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Boolean isVirtual;

    private List<ProductImageDTO> images; // 最新的图片列表
}
