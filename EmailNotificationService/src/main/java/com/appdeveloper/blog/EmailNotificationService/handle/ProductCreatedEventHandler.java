package com.appdeveloper.blog.EmailNotificationService.handle;

import com.appdeveloper.blog.EmailNotificationService.error.NotRetryableException;
import com.appdeveloper.blog.EmailNotificationService.error.RetryableException;
import com.appdeveloper.blog.EmailNotificationService.event.ProductCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
@KafkaListener(topics = "${kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "productCreatedEventKafkaListenerContainerFactory")
public class ProductCreatedEventHandler {

    private final RestTemplate restTemplate;

    public ProductCreatedEventHandler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @KafkaHandler
    public void handleProductCreatedEvent(ProductCreatedEvent productCreatedEvent) {
        log.info("Received product created event: {}", productCreatedEvent);

        String requestUrl = "localhost:8080";
        try {
            ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, null, String.class);
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info("Receiver response from the remote server: {}", response.getBody());
            }
        } catch (ResourceAccessException e) {
            log.error("ResourceAccessException ", e);
            throw new RetryableException(e);
        } catch (Exception e) {
            log.error("Exception ", e);
            throw new NotRetryableException(e);
        }
    }
}
