package com.notification.notificationservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.notification.notificationservice.kafka.event.NotificationEvent;
import com.notification.notificationservice.model.Notification;
import com.notification.notificationservice.model.Status;
import com.notification.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final ObjectMapper objectMapper;
    private final NotificationRepository notificationRepository;

    @KafkaListener(topics = "notifications", groupId = "notification-group")
    public void consume(String message) {
        try {
            NotificationEvent event = objectMapper.readValue(message, NotificationEvent.class);

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
