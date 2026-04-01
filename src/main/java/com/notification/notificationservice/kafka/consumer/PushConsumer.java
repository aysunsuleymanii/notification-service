package com.notification.notificationservice.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification.notificationservice.kafka.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PushConsumer {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "notifications", groupId = "push-group")
    public void consume(String message) throws Exception {
        NotificationEvent event = objectMapper.readValue(message, NotificationEvent.class);

        System.out.println("Sending PUSH to user " + event.getUserId() +
                ": " + event.getMessage());
    }
}
