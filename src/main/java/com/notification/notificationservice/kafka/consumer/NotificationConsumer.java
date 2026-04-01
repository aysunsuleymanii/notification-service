package com.notification.notificationservice.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification.notificationservice.kafka.event.NotificationEvent;
import com.notification.notificationservice.kafka.producer.NotificationProducer;
import com.notification.notificationservice.model.Notification;
import com.notification.notificationservice.model.Status;
import com.notification.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final ObjectMapper objectMapper;
    private final NotificationRepository notificationRepository;
    private final StringRedisTemplate redisTemplate;
    private final NotificationProducer notificationProducer;

    private final Random random = new Random();

    @KafkaListener(topics = "notifications", groupId = "notification-group")
    public void consume(String message) {
        processMessage(message);
    }

    @KafkaListener(topics = "notifications-retry", groupId = "notification-group")
    public void retry(String message) {
        processMessage(message);
    }

    private void processMessage(String message) {
        try {
            NotificationEvent event = objectMapper.readValue(message, NotificationEvent.class);

            String dedupKey = "event:" + event.getEventId();
            String rateKey = "rate:" + event.getUserId();

            if (Boolean.TRUE.equals(redisTemplate.hasKey(dedupKey))) {
                System.out.println("Duplicate event ignored: " + event.getEventId());
                return;
            }

            Long count = redisTemplate.opsForValue().increment(rateKey);

            if (count != null && count == 1) {
                redisTemplate.expire(rateKey, Duration.ofMinutes(1));
            }

            if (count != null && count > 5) {
                System.out.println("Rate limit exceeded for user: " + event.getUserId());
                return;
            }

            if (random.nextInt(10) < 3) {
                System.out.println("Simulated random failure");
                throw new RuntimeException("Random failure occurred");
            }

            Notification notification = Notification.builder()
                    .userId(event.getUserId())
                    .message(event.getMessage())
                    .status(Status.PENDING)
                    .createdAt(LocalDateTime.now())
                    .build();

            notificationRepository.save(notification);

            redisTemplate.opsForValue().set(dedupKey, "processed", Duration.ofMinutes(10));

            System.out.println("Processed event: " + event.getEventId());

        } catch (Exception e) {
            handleRetry(message);
        }
    }

    private void handleRetry(String message) {
        try {
            NotificationEvent event = objectMapper.readValue(message, NotificationEvent.class);

            if (event.getRetryCount() < 3) {
                event.setRetryCount(event.getRetryCount() + 1);

                System.out.println("Retrying event... attempt " + event.getRetryCount());

                String updatedMessage = objectMapper.writeValueAsString(event);
                notificationProducer.sendToRetry(updatedMessage);

            } else {
                System.out.println("Sending to DLQ");
                notificationProducer.sendToDLQ(message);
            }

        } catch (Exception ex) {
            throw new RuntimeException("Retry handling failed", ex);
        }
    }
}