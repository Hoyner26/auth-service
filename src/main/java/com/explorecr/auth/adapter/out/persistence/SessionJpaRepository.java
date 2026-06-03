package com.explorecr.auth.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface SessionJpaRepository extends JpaRepository<SessionEntity, UUID> {
    Optional<SessionEntity> findByToken(String token);

    void deleteByToken(String token);

    void deleteByUser_Id(UUID userId);

    void deleteByExpiresAtBefore(LocalDateTime now);
}
