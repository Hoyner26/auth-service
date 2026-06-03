package com.explorecr.auth.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    USER,
    ADMIN;

    @JsonCreator
    public static Role fromJson(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Role.valueOf(value.trim().toUpperCase());
    }

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }
}
