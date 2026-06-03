package com.explorecr.auth.domain.port.out;

import com.explorecr.auth.domain.model.Role;
import com.explorecr.auth.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {
    User save(User user);

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, UUID id);

    Page<User> findUsers(String search, Role role, Pageable pageable);

    void deleteById(UUID id);
}
