package com.explorecr.auth.domain.port.out;

import com.explorecr.auth.domain.model.Session;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface SessionRepositoryPort {
    Session save(Session session);

    Optional<Session> findByToken(String token);

    void deleteByToken(String token);

    void deleteByUserId(UUID userId);

    void deleteExpiredBefore(LocalDateTime now);
}
