package com.appdeveloper.blog.productService.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateProductReqDto {

    private String title;
    private BigDecimal price;
    private Integer quantity;
}
