package com.appdeveloper.blog.productService.services.impl;

import com.appdeveloper.blog.productService.dto.request.CreateProductReqDto;
import com.appdeveloper.blog.productService.event.ProductCreatedEvent;
import com.appdeveloper.blog.productService.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    public ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    // async
    @Override
    public String createProductAsync(CreateProductReqDto createProductReqDto) {
        String productId = UUID.randomUUID().toString();
        ProductCreatedEvent productCreatedEvent = ProductCreatedEvent.builder()
                .productId(productId)
                .price(createProductReqDto.getPrice())
                .quantity(createProductReqDto.getQuantity())
                .title(createProductReqDto.getTitle())
                .build();

        kafkaTemplate.send("product-created-event-topic", productId, productCreatedEvent);
        return productId;
    }

    @Override
    public String createProductSync(CreateProductReqDto createProductReqDto) throws ExecutionException, InterruptedException {
        String productId = UUID.randomUUID().toString();
        ProductCreatedEvent productCreatedEvent = ProductCreatedEvent.builder()
                .productId(productId)
                .price(createProductReqDto.getPrice())
                .quantity(createProductReqDto.getQuantity())
                .title(createProductReqDto.getTitle())
                .build();

        log.info("Before sending message to Kafka");
        SendResult<String, ProductCreatedEvent> result = kafkaTemplate.send("product-created-event-topic", productId, productCreatedEvent).get();
        log.info("After sending message to Kafka");
        log.info("Message sent to Kafka with offset: {}", result.getRecordMetadata().offset());
        log.info("Message sent to Kafka with partition: {}", result.getRecordMetadata().partition());
        log.info("Message sent to Kafka with topic: {}", result.getRecordMetadata().topic());

        log.info("Return product id: {}", productId);
        return productId;
    }
}
