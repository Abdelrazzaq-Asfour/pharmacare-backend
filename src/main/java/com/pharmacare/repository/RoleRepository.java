package com.pharmacare.repository;

import com.pharmacare.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository for system roles and RBAC permission binding.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Byte> {

    Optional<Role> findByRoleName(String roleName);
}