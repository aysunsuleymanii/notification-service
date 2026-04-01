package com.notification.notificationservice.controller;

import com.notification.notificationservice.controller.dto.NotificationRequest;
import com.notification.notificationservice.model.Notification;
import com.notification.notificationservice.service.NotificationQueryService;
import com.notification.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationQueryService queryService;


    @PostMapping
    public Notification create(@RequestBody NotificationRequest request) {
        return notificationService.createNotification(
                request.getUserId(),
                request.getMessage()
        );
    }

    @GetMapping("/{userId}")
    public List<Notification> getUserNotifications(@PathVariable String userId) {
        return queryService.getByUserId(userId);
    }

    @PutMapping("/{id}/read")
    public Notification markAsRead(@PathVariable UUID id) {
        return queryService.markAsRead(id);
    }
}
