package com.explorecr.auth.adapter.out.persistence;

import com.explorecr.auth.adapter.in.web.dto.response.UserResponse;
import com.explorecr.auth.domain.model.User;

public final class UserMapper {
    private UserMapper() {
    }

    public static User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return new User(
            entity.getId(),
            entity.getName(),
            entity.getEmail(),
            entity.getPasswordHash(),
            entity.getRole(),
            entity.getAvatarUrl(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public static UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.id());
        entity.setName(user.name());
        entity.setEmail(user.email());
        entity.setPasswordHash(user.passwordHash());
        entity.setRole(user.role());
        entity.setAvatarUrl(user.avatarUrl());
        entity.setCreatedAt(user.createdAt());
        entity.setUpdatedAt(user.updatedAt());
        return entity;
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(
            user.id(),
            user.name(),
            user.email(),
            user.role().name().toLowerCase(),
            user.avatarUrl(),
            user.createdAt()
        );
    }
}
