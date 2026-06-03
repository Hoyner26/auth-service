package com.explorecr.auth.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record User(
    UUID id,
    String name,
    String email,
    String passwordHash,
    Role role,
    String avatarUrl,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
