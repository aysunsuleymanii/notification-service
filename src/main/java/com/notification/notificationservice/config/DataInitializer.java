package com.notification.notificationservice.config;

import com.notification.notificationservice.model.UserPreferences;
import com.notification.notificationservice.repository.UserPreferencesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserPreferencesRepository repository;

    @Override
    public void run(String... args) {
        repository.save(UserPreferences.builder()
                .userId("42")
                .emailEnabled(true)
                .pushEnabled(false)
                .inAppEnabled(true)
                .build());
    }
}
