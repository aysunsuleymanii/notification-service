package com.notification.notificationservice.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification.notificationservice.kafka.event.NotificationEvent;
import com.notification.notificationservice.repository.UserPreferencesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EmailConsumer {

    private final ObjectMapper objectMapper;
    private final UserPreferencesRepository preferencesRepository;

    @KafkaListener(topics = "notifications", groupId = "email-group")
    public void consume(String message) throws Exception {
        NotificationEvent event = objectMapper.readValue(message, NotificationEvent.class);

        var prefs = preferencesRepository.findById(event.getUserId()).orElse(null);

        if (prefs == null || !prefs.isEmailEnabled()) {
            System.out.println("Email disabled for user: " + event.getUserId());
            return;
        }

        System.out.println("Sending EMAIL to user " + event.getUserId());
    }
}