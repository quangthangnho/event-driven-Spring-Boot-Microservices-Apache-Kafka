package com.appdeveloper.blog.EmailNotificationService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.appdeveloper.blog.EmailNotificationService.entity.ProgressEventEntity;

/**
 * @author admin
 */
@Repository
public interface ProgressEventRepository extends JpaRepository<ProgressEventEntity, Long> {
    ProgressEventEntity findByMessageId(String messageId);
}
