package com.notification.notificationservice.controller;

import com.notification.notificationservice.controller.dto.UserPreferencesRequest;
import com.notification.notificationservice.model.UserPreferences;
import com.notification.notificationservice.service.UserPreferencesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/preferences")
@RequiredArgsConstructor
public class UserPreferencesController {

    private final UserPreferencesService service;

    @PostMapping
    public UserPreferences create(@RequestBody UserPreferencesRequest request) {
        UserPreferences prefs = UserPreferences.builder()
                .userId(request.getUserId())
                .emailEnabled(request.isEmailEnabled())
                .pushEnabled(request.isPushEnabled())
                .inAppEnabled(request.isInAppEnabled())
                .build();

        return service.save(prefs);
    }

    @GetMapping("/{userId}")
    public UserPreferences get(@PathVariable String userId) {
        return service.getByUserId(userId);
    }

    @PutMapping("/{userId}")
    public UserPreferences update(@PathVariable String userId,
                                  @RequestBody UserPreferencesRequest request) {

        UserPreferences prefs = UserPreferences.builder()
                .userId(userId)
                .emailEnabled(request.isEmailEnabled())
                .pushEnabled(request.isPushEnabled())
                .inAppEnabled(request.isInAppEnabled())
                .build();

        return service.save(prefs);
    }
}