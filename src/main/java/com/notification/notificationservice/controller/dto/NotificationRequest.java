package com.notification.notificationservice.controller.dto;

import lombok.Data;

@Data
public class NotificationRequest {
    private String userId;
    private String message;
}