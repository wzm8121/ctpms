package com.mzw.ctpmsbackend.dto;

import lombok.Data;

import java.util.List;

@Data
public class CategoryTreeDTO {
    private Integer categoryId;
    private String name;
    private Integer parentId;
    private Boolean isVirtual;
    private List<CategoryTreeDTO> children; // 子分类
}
