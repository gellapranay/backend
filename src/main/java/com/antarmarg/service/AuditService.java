package com.antarmarg.service;

import com.antarmarg.entity.AuditEvent;
import com.antarmarg.repository.AuditEventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditService {

    private final AuditEventRepository auditEventRepository;

    public AuditService(AuditEventRepository auditEventRepository) {
        this.auditEventRepository = auditEventRepository;
    }

    public void logAction(String username, String action, String entityType, String entityId, String details, String ip) {
        AuditEvent event = new AuditEvent(username, action, entityType, entityId, details, ip);
        auditEventRepository.save(event);
    }

    public List<AuditEvent> getRecentEvents() {
        return auditEventRepository.findTop100ByOrderByCreatedAtDesc();
    }
}
