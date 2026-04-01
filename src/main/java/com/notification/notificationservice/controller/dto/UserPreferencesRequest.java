package com.notification.notificationservice.controller.dto;

import lombok.Data;

@Data
public class UserPreferencesRequest {
    private String userId;
    private boolean emailEnabled;
    private boolean pushEnabled;
    private boolean inAppEnabled;
}
