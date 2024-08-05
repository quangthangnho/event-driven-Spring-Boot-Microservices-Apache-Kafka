package com.appdeveloper.blog.productService.services;

import com.appdeveloper.blog.productService.dto.request.CreateProductReqDto;

import java.util.concurrent.ExecutionException;

public interface ProductService {

    String createProductAsync(CreateProductReqDto createProductReqDto);
    String createProductSync(CreateProductReqDto createProductReqDto) throws ExecutionException, InterruptedException;
}
