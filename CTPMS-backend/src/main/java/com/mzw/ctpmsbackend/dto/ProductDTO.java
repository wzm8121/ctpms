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
    @ApiModelProperty(value = "商品ID", example = "1")
    private Integer productId;

    @NotBlank(message = "商品标题不能为空")
    @ApiModelProperty(value = "商品标题", required = true, example = "二手笔记本电脑")
    private String title;

    @ApiModelProperty(value = "商品描述", example = "九成新，配置：i7 16G 512G")
    private String description;

    @NotNull(message = "分类ID不能为空")
    @ApiModelProperty(value = "分类ID", required = true, example = "1")
    private Long categoryId;

    @NotNull(message = "价格不能为空")
    @PositiveOrZero(message = "价格不能为负数")
    @ApiModelProperty(value = "商品价格", required = true, example = "2999.99")
    private BigDecimal price;

    @NotNull(message = "库存不能为空")
    @PositiveOrZero(message = "库存不能为负数")
    @ApiModelProperty(value = "商品库存", required = true, example = "10")
    private Integer stock;

    @ApiModelProperty(value = "是否为虚拟商品", example = "false")
    private Boolean isVirtual;

    @ApiModelProperty(value = "商品图片URL列表")
    private List<ProductImageDTO> imageUrls;
}
