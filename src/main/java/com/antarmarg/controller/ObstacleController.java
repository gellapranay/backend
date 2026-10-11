package com.antarmarg.controller;

import com.antarmarg.dto.request.CreateObstacleRequest;
import com.antarmarg.dto.response.ApiResponse;
import com.antarmarg.dto.response.ObstacleDto;
import com.antarmarg.service.ObstacleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/obstacles")
public class ObstacleController {

    private final ObstacleService obstacleService;

    public ObstacleController(ObstacleService obstacleService) {
        this.obstacleService = obstacleService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ObstacleDto>> reportObstacle(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CreateObstacleRequest request) {
        String email = userDetails != null ? userDetails.getUsername() : null;
        ObstacleDto created = obstacleService.reportObstacle(email, request);
        return ResponseEntity.ok(ApiResponse.ok("Obstacle reported successfully; graph updated in real-time", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ObstacleDto>>> getActiveObstacles() {
        List<ObstacleDto> obstacles = obstacleService.getActiveObstacles();
        return ResponseEntity.ok(ApiResponse.ok(obstacles));
    }

    @GetMapping("/facility/{facilityId}")
    public ResponseEntity<ApiResponse<List<ObstacleDto>>> getFacilityObstacles(@PathVariable Long facilityId) {
        List<ObstacleDto> obstacles = obstacleService.getFacilityObstacles(facilityId);
        return ResponseEntity.ok(ApiResponse.ok(obstacles));
    }

    @PutMapping("/{id}/resolve")
    @PreAuthorize("hasAnyRole('FACILITY_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ObstacleDto>> resolveObstacle(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails != null ? userDetails.getUsername() : "system";
        ObstacleDto resolved = obstacleService.resolveObstacle(id, email);
        return ResponseEntity.ok(ApiResponse.ok("Obstacle resolved; path reopened successfully", resolved));
    }
}
