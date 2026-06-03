package com.explorecr.auth.adapter.in.web.dto.response;

public record AuthResponse(
    String token,
    UserResponse user
) {
}
