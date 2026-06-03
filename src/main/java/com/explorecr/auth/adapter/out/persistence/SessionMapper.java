package com.explorecr.auth.adapter.out.persistence;

import com.explorecr.auth.domain.model.Session;

public final class SessionMapper {
    private SessionMapper() {
    }

    public static Session toDomain(SessionEntity entity) {
        return new Session(
            entity.getId(),
            UserMapper.toDomain(entity.getUser()),
            entity.getToken(),
            entity.getExpiresAt(),
            entity.getCreatedAt()
        );
    }
}
