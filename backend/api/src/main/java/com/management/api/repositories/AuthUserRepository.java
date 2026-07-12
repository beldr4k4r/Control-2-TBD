package com.management.api.repositories;

import com.management.api.models.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
    boolean existsByUsername(String username);
    Optional<AuthUser> findByUsername(String username);
}
