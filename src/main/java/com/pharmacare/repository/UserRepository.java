package com.pharmacare.repository;

import com.pharmacare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository for managing application users and authentication lookups.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Fast lookup for authentication security filters
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}