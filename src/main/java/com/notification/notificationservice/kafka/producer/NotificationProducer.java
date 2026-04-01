package com.notification.notificationservice.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification.notificationservice.kafka.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String MAIN_TOPIC = "notifications";
    private static final String RETRY_TOPIC = "notifications-retry";
    private static final String DLQ_TOPIC = "notifications-dlq";

    public void send(NotificationEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(MAIN_TOPIC, event.getEventId(), message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send event to main topic", e);
        }
    }

    public void sendToRetry(String message) {
        kafkaTemplate.send(RETRY_TOPIC, message);
    }

    public void sendToDLQ(String message) {
        kafkaTemplate.send(DLQ_TOPIC, message);
    }
}