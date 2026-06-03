package com.explorecr.auth.application.service;

import com.explorecr.auth.adapter.in.web.dto.request.UpdateUserRequest;
import com.explorecr.auth.adapter.in.web.dto.response.PagedResponse;
import com.explorecr.auth.adapter.in.web.dto.response.UserResponse;
import com.explorecr.auth.adapter.out.persistence.UserMapper;
import com.explorecr.auth.application.exception.SelfDeleteException;
import com.explorecr.auth.application.exception.UserAlreadyExistsException;
import com.explorecr.auth.application.exception.UserNotFoundException;
import com.explorecr.auth.domain.model.Role;
import com.explorecr.auth.domain.model.User;
import com.explorecr.auth.domain.port.in.UserUseCase;
import com.explorecr.auth.domain.port.out.SessionRepositoryPort;
import com.explorecr.auth.domain.port.out.UserRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService implements UserUseCase {
    private final UserRepositoryPort userRepository;
    private final SessionRepositoryPort sessionRepository;

    public UserService(UserRepositoryPort userRepository, SessionRepositoryPort sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<UserResponse> listUsers(int page, int pageSize, String search, Role role) {
        int safePage = Math.max(page, 1);
        int safePageSize = Math.min(Math.max(pageSize, 1), 100);
        Pageable pageable = PageRequest.of(safePage - 1, safePageSize);
        Page<UserResponse> users = userRepository.findUsers(search, role, pageable)
            .map(UserMapper::toResponse);

        return new PagedResponse<>(
            users.getContent(),
            users.getTotalElements(),
            safePage,
            safePageSize,
            users.getTotalPages()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUser(UUID id) {
        return UserMapper.toResponse(findUser(id));
    }

    @Override
    @Transactional
    public UserResponse updateUser(UUID id, UpdateUserRequest request) {
        User user = findUser(id);
        String email = request.email() == null ? user.email() : request.email().trim().toLowerCase();
        if (!email.equalsIgnoreCase(user.email()) && userRepository.existsByEmailAndIdNot(email, id)) {
            throw new UserAlreadyExistsException();
        }

        User updated = new User(
            user.id(),
            request.name() == null || request.name().isBlank() ? user.name() : request.name().trim(),
            email,
            user.passwordHash(),
            request.role() == null ? user.role() : request.role(),
            request.avatar() == null ? user.avatarUrl() : request.avatar(),
            user.createdAt(),
            user.updatedAt()
        );

        return UserMapper.toResponse(userRepository.save(updated));
    }

    @Override
    @Transactional
    public void deleteUser(UUID id, User currentUser) {
        if (currentUser != null && currentUser.id().equals(id)) {
            throw new SelfDeleteException();
        }

        findUser(id);
        sessionRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }

    private User findUser(UUID id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }
}
