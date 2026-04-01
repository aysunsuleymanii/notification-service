package com.notification.notificationservice.repository;

import com.notification.notificationservice.model.UserPreferences;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPreferencesRepository extends JpaRepository<UserPreferences, String> {
}
