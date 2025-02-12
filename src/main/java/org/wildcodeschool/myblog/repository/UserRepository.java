package org.wildcodeschool.myblog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wildcodeschool.myblog.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
