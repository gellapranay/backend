package com.antarmarg.controller;

import com.antarmarg.dto.response.AnalyticsOverviewDto;
import com.antarmarg.dto.response.ApiResponse;
import com.antarmarg.entity.AuditEvent;
import com.antarmarg.service.AnalyticsService;
import com.antarmarg.service.AuditService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final AuditService auditService;

    public AnalyticsController(AnalyticsService analyticsService, AuditService auditService) {
        this.analyticsService = analyticsService;
        this.auditService = auditService;
    }

    @GetMapping("/overview")
    public ResponseEntity<ApiResponse<AnalyticsOverviewDto>> getOverview() {
        AnalyticsOverviewDto metrics = analyticsService.getOverviewMetrics();
        return ResponseEntity.ok(ApiResponse.ok(metrics));
    }

    @GetMapping("/audit-events")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<AuditEvent>>> getAuditEvents() {
        List<AuditEvent> events = auditService.getRecentEvents();
        return ResponseEntity.ok(ApiResponse.ok(events));
    }
}
