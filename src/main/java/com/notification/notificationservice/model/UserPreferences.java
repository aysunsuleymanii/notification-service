package com.notification.notificationservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreferences {

    @Id
    private String userId;

    private boolean emailEnabled;
    private boolean pushEnabled;
    private boolean inAppEnabled;
}
