package com.explorecr.auth.adapter.out.persistence;

import com.explorecr.auth.domain.model.Session;
import com.explorecr.auth.domain.port.out.SessionRepositoryPort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SessionRepositoryAdapter implements SessionRepositoryPort {
    private final SessionJpaRepository sessionRepository;
    private final UserJpaRepository userRepository;

    public SessionRepositoryAdapter(SessionJpaRepository sessionRepository, UserJpaRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Session save(Session session) {
        UserEntity user = userRepository.findById(session.user().id()).orElseThrow();
        SessionEntity entity = new SessionEntity();
        entity.setId(session.id());
        entity.setUser(user);
        entity.setToken(session.token());
        entity.setExpiresAt(session.expiresAt());
        entity.setCreatedAt(session.createdAt());
        return SessionMapper.toDomain(sessionRepository.save(entity));
    }

    @Override
    public Optional<Session> findByToken(String token) {
        return sessionRepository.findByToken(token).map(SessionMapper::toDomain);
    }

    @Override
    @Transactional
    public void deleteByToken(String token) {
        sessionRepository.deleteByToken(token);
    }

    @Override
    @Transactional
    public void deleteByUserId(UUID userId) {
        sessionRepository.deleteByUser_Id(userId);
    }

    @Override
    @Transactional
    public void deleteExpiredBefore(LocalDateTime now) {
        sessionRepository.deleteByExpiresAtBefore(now);
    }
}
