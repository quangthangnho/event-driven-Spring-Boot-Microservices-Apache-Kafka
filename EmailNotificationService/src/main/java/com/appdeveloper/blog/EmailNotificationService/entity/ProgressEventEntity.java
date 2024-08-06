package com.appdeveloper.blog.EmailNotificationService.entity;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(
        name = "progress_event",
        indexes = {
            @Index(name = "idx_message_id", columnList = "message_id"),
            @Index(name = "idx_product_id", columnList = "product_id")
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgressEventEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message_id", nullable = false)
    private String messageId;

    @Column(name = "product_id", nullable = false)
    private String productId;
}
