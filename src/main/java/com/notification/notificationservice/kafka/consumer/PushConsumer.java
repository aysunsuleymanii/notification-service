package com.notification.notificationservice.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification.notificationservice.kafka.event.NotificationEvent;
import com.notification.notificationservice.repository.UserPreferencesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PushConsumer {

    private final ObjectMapper objectMapper;
    private final UserPreferencesRepository preferencesRepository;


    @KafkaListener(topics = "notifications", groupId = "push-group")
    public void consume(String message) throws Exception {
        NotificationEvent event = objectMapper.readValue(message, NotificationEvent.class);

        var prefs = preferencesRepository.findById(event.getUserId()).orElse(null);
        if (prefs == null || !prefs.isPushEnabled()) {
            return;
        }

        System.out.println("Sending PUSH to user " + event.getUserId() +
                ": " + event.getMessage());
    }
}
