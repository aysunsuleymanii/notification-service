package com.notification.notificationservice.service;

import com.notification.notificationservice.model.UserPreferences;

public interface UserPreferencesService {
    UserPreferences save(UserPreferences preferences);

    UserPreferences getByUserId(String userId);
}
