package com.explorecr.auth.adapter.in.web;

import com.explorecr.auth.adapter.in.web.dto.request.LoginRequest;
import com.explorecr.auth.adapter.in.web.dto.request.RegisterRequest;
import com.explorecr.auth.adapter.in.web.dto.response.ApiDataResponse;
import com.explorecr.auth.adapter.in.web.dto.response.AuthResponse;
import com.explorecr.auth.adapter.in.web.dto.response.MessageResponse;
import com.explorecr.auth.adapter.in.web.dto.response.UserResponse;
import com.explorecr.auth.adapter.out.persistence.UserMapper;
import com.explorecr.auth.domain.model.User;
import com.explorecr.auth.domain.port.in.AuthUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthUseCase authUseCase;

    public AuthController(AuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiDataResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return new ApiDataResponse<>(authUseCase.register(request));
    }

    @PostMapping("/login")
    public ApiDataResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return new ApiDataResponse<>(authUseCase.login(request));
    }

    @GetMapping("/me")
    public ApiDataResponse<UserResponse> me(@AuthenticationPrincipal User user) {
        return new ApiDataResponse<>(UserMapper.toResponse(user));
    }

    @PostMapping("/logout")
    public MessageResponse logout(@RequestHeader("Authorization") String authorizationHeader) {
        authUseCase.logout(authorizationHeader.substring("Bearer ".length()));
        return new MessageResponse("Logged out successfully");
    }
}
