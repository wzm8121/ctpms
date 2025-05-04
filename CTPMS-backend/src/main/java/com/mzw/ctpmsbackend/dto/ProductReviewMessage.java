package com.mzw.ctpmsbackend.dto;

import lombok.Data;

@Data
public class ProductReviewMessage {
    private Integer productId;
    private String title;
    private String description;
    private String tags;
}
