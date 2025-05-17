package com.mzw.ctpmsbackend.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品数据传输对象
 */
@Data
@ApiModel("商品信息")
public class ProductDTO {
    private Integer productId;
    private String title;
    private String description;
    private Integer categoryId;
    private Integer status;
    private BigDecimal price;
    private Integer stock;
    private Integer isVirtual;
    private List<ProductImageDTO> images;
}
