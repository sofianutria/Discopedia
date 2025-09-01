package com.example.discopedia.discopedia.users;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername (String username);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
