package com.explorecr.auth.domain.port.in;

import com.explorecr.auth.adapter.in.web.dto.request.LoginRequest;
import com.explorecr.auth.adapter.in.web.dto.request.RegisterRequest;
import com.explorecr.auth.adapter.in.web.dto.response.AuthResponse;

public interface AuthUseCase {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    void logout(String token);
}
