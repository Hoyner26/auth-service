package com.explorecr.auth.application.service;

import com.explorecr.auth.adapter.in.web.dto.request.LoginRequest;
import com.explorecr.auth.adapter.in.web.dto.request.RegisterRequest;
import com.explorecr.auth.adapter.in.web.dto.response.AuthResponse;
import com.explorecr.auth.adapter.out.persistence.UserMapper;
import com.explorecr.auth.application.exception.InvalidCredentialsException;
import com.explorecr.auth.application.exception.PasswordMismatchException;
import com.explorecr.auth.application.exception.UserAlreadyExistsException;
import com.explorecr.auth.domain.model.Role;
import com.explorecr.auth.domain.model.Session;
import com.explorecr.auth.domain.model.User;
import com.explorecr.auth.domain.port.in.AuthUseCase;
import com.explorecr.auth.domain.port.out.SessionRepositoryPort;
import com.explorecr.auth.domain.port.out.UserRepositoryPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService implements AuthUseCase {
    private final UserRepositoryPort userRepository;
    private final SessionRepositoryPort sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final long sessionExpirationHours;

    public AuthService(
        UserRepositoryPort userRepository,
        SessionRepositoryPort sessionRepository,
        PasswordEncoder passwordEncoder,
        @Value("${app.session.expiration-hours}") long sessionExpirationHours
    ) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
        this.sessionExpirationHours = sessionExpirationHours;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException();
        }
        if (!request.password().equals(request.confirmPassword())) {
            throw new PasswordMismatchException();
        }

        User user = new User(
            null,
            request.name().trim(),
            email,
            passwordEncoder.encode(request.password()),
            Role.USER,
            null,
            null,
            null
        );

        User saved = userRepository.save(user);
        return createSession(saved);
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email().trim().toLowerCase())
            .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.passwordHash())) {
            throw new InvalidCredentialsException();
        }

        return createSession(user);
    }

    @Override
    @Transactional
    public void logout(String token) {
        sessionRepository.deleteByToken(token);
    }

    private AuthResponse createSession(User user) {
        LocalDateTime now = LocalDateTime.now();
        Session session = new Session(
            null,
            user,
            UUID.randomUUID().toString(),
            now.plusHours(sessionExpirationHours),
            now
        );
        Session saved = sessionRepository.save(session);
        return new AuthResponse(saved.token(), UserMapper.toResponse(user));
    }
}
