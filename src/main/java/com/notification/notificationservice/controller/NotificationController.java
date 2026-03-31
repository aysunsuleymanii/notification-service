package com.notification.notificationservice.controller;

import com.notification.notificationservice.controller.dto.NotificationRequest;
import com.notification.notificationservice.model.Notification;
import com.notification.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public Notification create(@RequestBody NotificationRequest request) {
        return notificationService.createNotification(
                request.getUserId(),
                request.getMessage()
        );
    }
}
