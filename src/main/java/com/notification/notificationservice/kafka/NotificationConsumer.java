package com.notification.notificationservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.notification.notificationservice.kafka.event.NotificationEvent;
import com.notification.notificationservice.model.Notification;
import com.notification.notificationservice.model.Status;
import com.notification.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.core.StringRedisTemplate;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final ObjectMapper objectMapper;
    private final NotificationRepository notificationRepository;
    private final StringRedisTemplate redisTemplate;

    @KafkaListener(topics = "notifications", groupId = "notification-group")
    public void consume(String message) {
        try {
            NotificationEvent event = objectMapper.readValue(message, NotificationEvent.class);

            String key = "event:" + event.getEventId();

            Boolean exists = redisTemplate.hasKey(key);

            if (Boolean.TRUE.equals(exists)) {
                System.out.println("Duplicate event ignored: " + event.getEventId());
                return;
            }

            redisTemplate.opsForValue().set(key, "processed", Duration.ofMinutes(10));

            Notification notification = Notification.builder()
                    .userId(event.getUserId())
                    .message(event.getMessage())
                    .status(Status.PENDING)
                    .createdAt(LocalDateTime.now())
                    .build();

            notificationRepository.save(notification);

            System.out.println("Processed event: " + event.getEventId());

        } catch (Exception e) {
            throw new RuntimeException("Failed to process event", e);
        }
    }
}
