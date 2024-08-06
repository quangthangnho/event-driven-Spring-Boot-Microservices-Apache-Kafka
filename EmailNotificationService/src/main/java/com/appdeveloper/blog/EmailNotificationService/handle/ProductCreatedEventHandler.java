package com.appdeveloper.blog.EmailNotificationService.handle;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.appdeveloper.blog.EmailNotificationService.entity.ProgressEventEntity;
import com.appdeveloper.blog.EmailNotificationService.error.NotRetryableException;
import com.appdeveloper.blog.EmailNotificationService.error.RetryableException;
import com.appdeveloper.blog.EmailNotificationService.event.ProductCreatedEvent;
import com.appdeveloper.blog.EmailNotificationService.repositories.ProgressEventRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@KafkaListener(
        topics = "${kafka.topic.name}",
        groupId = "${spring.kafka.consumer.group-id}",
        containerFactory = "productCreatedEventKafkaListenerContainerFactory")
public class ProductCreatedEventHandler {

    private final RestTemplate restTemplate;

    private final ProgressEventRepository progressEventRepository;

    public ProductCreatedEventHandler(RestTemplate restTemplate, ProgressEventRepository progressEventRepository) {
        this.restTemplate = restTemplate;
        this.progressEventRepository = progressEventRepository;
    }

    @Transactional
    @KafkaHandler
    public void handleProductCreatedEvent(
            @Payload ProductCreatedEvent productCreatedEvent,
            @Header("messageId") String messageId,
            @Header(KafkaHeaders.RECEIVED_KEY) String messageKey) {
        log.info("Received product created event: {}", productCreatedEvent);
        log.info("MessageId: {}", messageId);
        log.info("MessageKey: {}", messageKey);

        if (progressEventRepository.findByMessageId(messageId) != null) {
            log.info("Message already processed: {}", messageId);
            return;
        }

//        try {
//            ResponseEntity<String> response =
//                    restTemplate.exchange("localhost:8080", HttpMethod.GET, null, String.class);
//            if (response.getStatusCode().is2xxSuccessful()) {
//                log.info("Receiver response from the remote server: {}", response.getBody());
//            }
//        } catch (ResourceAccessException e) {
//            log.error("ResourceAccessException ", e);
//            throw new RetryableException(e);
//        } catch (Exception e) {
//            log.error("Exception ", e);
//            throw new NotRetryableException(e);
//        }

        try {
            progressEventRepository.save(ProgressEventEntity.builder()
                    .messageId(messageId)
                    .productId(productCreatedEvent.getProductId())
                    .build());
        } catch (Exception e) {
            log.error("Error saving progress event", e);
            throw new NotRetryableException(e);
        }
    }
}
