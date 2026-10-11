package com.antarmarg.repository;

import com.antarmarg.entity.Role;
import com.antarmarg.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    List<User> findByRole(Role role);
    List<User> findByRoleAndIsApproved(Role role, Boolean isApproved);
    long countByRole(Role role);
}
