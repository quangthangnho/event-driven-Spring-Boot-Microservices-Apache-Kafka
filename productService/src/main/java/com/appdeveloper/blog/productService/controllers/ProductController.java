package com.appdeveloper.blog.productService.controllers;

import com.appdeveloper.blog.productService.dto.request.CreateProductReqDto;
import com.appdeveloper.blog.productService.dto.response.ErrorMessage;
import com.appdeveloper.blog.productService.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create-async")
    public ResponseEntity<String> createProductAsync(@RequestBody CreateProductReqDto createProductReqDto) {
        return new ResponseEntity<>(productService.createProductAsync(createProductReqDto), HttpStatus.CREATED);
    }

    @PostMapping("/create-sync")
    public ResponseEntity<Object> createProductSync(@RequestBody CreateProductReqDto createProductReqDto) throws ExecutionException, InterruptedException {
        String productId;
        try {
            productId = productService.createProductSync(createProductReqDto);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorMessage(e.getMessage(), "/product"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(productId, HttpStatus.CREATED);
    }
}
