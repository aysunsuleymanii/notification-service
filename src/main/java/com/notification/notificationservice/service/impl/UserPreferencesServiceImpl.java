package com.notification.notificationservice.service.impl;

import com.notification.notificationservice.model.UserPreferences;
import com.notification.notificationservice.repository.UserPreferencesRepository;
import com.notification.notificationservice.service.UserPreferencesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPreferencesServiceImpl implements UserPreferencesService {

    private final UserPreferencesRepository repository;

    @Override
    public UserPreferences save(UserPreferences preferences) {
        return repository.save(preferences);
    }

    @Override
    public UserPreferences getByUserId(String userId) {
        return repository.findById(userId).orElse(null);
    }
}
