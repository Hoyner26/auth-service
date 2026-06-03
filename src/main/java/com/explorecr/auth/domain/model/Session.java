package com.explorecr.auth.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record Session(
    UUID id,
    User user,
    String token,
    LocalDateTime expiresAt,
    LocalDateTime createdAt
) {
    public boolean isExpired(LocalDateTime now) {
        return !expiresAt.isAfter(now);
    }
}
