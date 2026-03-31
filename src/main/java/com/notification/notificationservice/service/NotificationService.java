package com.notification.notificationservice.service;

import com.notification.notificationservice.model.Notification;

public interface NotificationService {
    Notification createNotification(String userId, String message);
}
