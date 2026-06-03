package com.explorecr.auth.adapter.out.persistence;

import com.explorecr.auth.domain.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, UUID id);

    @Query("""
        select u from UserEntity u
        where (:role is null or u.role = :role)
          and (:search is null
            or lower(u.name) like lower(concat('%', :search, '%'))
            or lower(u.email) like lower(concat('%', :search, '%')))
        order by u.createdAt desc
        """)
    Page<UserEntity> searchUsers(@Param("search") String search, @Param("role") Role role, Pageable pageable);
}
