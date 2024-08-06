package com.appdeveloper.blog.EmailNotificationService.handle;

import com.appdeveloper.blog.EmailNotificationService.event.ProductCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@KafkaListener(topics = "${kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "productCreatedEventKafkaListenerContainerFactory")
public class ProductCreatedEventHandler {

    @KafkaHandler
    public void handleProductCreatedEvent(ProductCreatedEvent productCreatedEvent) {
        log.info("Received product created event: {}", productCreatedEvent);

    }
}
