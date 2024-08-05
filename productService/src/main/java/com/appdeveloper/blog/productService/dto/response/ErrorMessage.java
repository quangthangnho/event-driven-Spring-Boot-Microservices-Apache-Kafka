package com.appdeveloper.blog.productService.dto.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {
    private Date timestamp;
    private String message;
    private String details;


    public ErrorMessage(String message, String details) {
        this.timestamp = new Date();
        this.message = message;
        this.details = details;
    }
}
