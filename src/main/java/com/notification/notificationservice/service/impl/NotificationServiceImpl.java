package com.notification.notificationservice.service.impl;

import com.notification.notificationservice.kafka.event.NotificationEvent;
import com.notification.notificationservice.kafka.producer.NotificationProducer;
import com.notification.notificationservice.model.Notification;
import com.notification.notificationservice.model.Status;
import com.notification.notificationservice.repository.NotificationRepository;
import com.notification.notificationservice.service.NotificationService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationProducer notificationProducer;

    @Override
    public Notification createNotification(String userId, String message) {
        NotificationEvent event = NotificationEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .userId(userId)
                .message(message)
                .build();

        notificationProducer.send(event);

        return Notification.builder()
                .userId(userId)
                .message(message)
                .status(null)
                .createdAt(null)
                .build();
    }
}

