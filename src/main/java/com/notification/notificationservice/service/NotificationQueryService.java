package com.notification.notificationservice.service;

import com.notification.notificationservice.model.Notification;

import java.util.List;
import java.util.UUID;

public interface NotificationQueryService {
    List<Notification> getByUserId(String userId);
    Notification markAsRead(UUID id);
}