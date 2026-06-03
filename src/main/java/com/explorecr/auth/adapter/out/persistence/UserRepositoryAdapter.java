package com.explorecr.auth.adapter.out.persistence;

import com.explorecr.auth.domain.model.Role;
import com.explorecr.auth.domain.model.User;
import com.explorecr.auth.domain.port.out.UserRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryAdapter implements UserRepositoryPort {
    private final UserJpaRepository repository;

    public UserRepositoryAdapter(UserJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        return UserMapper.toDomain(repository.save(UserMapper.toEntity(user)));
    }

    @Override
    public Optional<User> findById(UUID id) {
        return repository.findById(id).map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmailIgnoreCase(email).map(UserMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmailIgnoreCase(email);
    }

    @Override
    public boolean existsByEmailAndIdNot(String email, UUID id) {
        return repository.existsByEmailIgnoreCaseAndIdNot(email, id);
    }

    @Override
    public Page<User> findUsers(String search, Role role, Pageable pageable) {
        String normalizedSearch = search == null || search.isBlank() ? null : search.trim();
        return repository.searchUsers(normalizedSearch, role, pageable).map(UserMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
