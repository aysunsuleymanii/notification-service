package com.notification.notificationservice.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification.notificationservice.kafka.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InAppConsumer {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "notifications", groupId = "inapp-group")
    public void consume(String message) throws Exception {
        NotificationEvent event = objectMapper.readValue(message, NotificationEvent.class);

        System.out.println("Saving IN-APP notification for user " + event.getUserId());
    }
}
