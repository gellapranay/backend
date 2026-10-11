package com.antarmarg.repository;

import com.antarmarg.entity.AuditEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditEventRepository extends JpaRepository<AuditEvent, Long> {
    List<AuditEvent> findTop100ByOrderByCreatedAtDesc();
    List<AuditEvent> findByUsernameOrderByCreatedAtDesc(String username);
}
