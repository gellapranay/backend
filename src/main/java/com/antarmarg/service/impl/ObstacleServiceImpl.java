package com.antarmarg.service.impl;

import com.antarmarg.dto.request.CreateObstacleRequest;
import com.antarmarg.dto.response.ObstacleDto;
import com.antarmarg.entity.*;
import com.antarmarg.exception.ResourceNotFoundException;
import com.antarmarg.repository.*;
import com.antarmarg.service.ObstacleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ObstacleServiceImpl implements ObstacleService {

    private final ObstacleRepository obstacleRepository;
    private final FacilityRepository facilityRepository;
    private final LocationNodeRepository nodeRepository;
    private final LocationEdgeRepository edgeRepository;
    private final UserRepository userRepository;

    public ObstacleServiceImpl(
            ObstacleRepository obstacleRepository,
            FacilityRepository facilityRepository,
            LocationNodeRepository nodeRepository,
            LocationEdgeRepository edgeRepository,
            UserRepository userRepository) {
        this.obstacleRepository = obstacleRepository;
        this.facilityRepository = facilityRepository;
        this.nodeRepository = nodeRepository;
        this.edgeRepository = edgeRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public ObstacleDto reportObstacle(String reporterEmail, CreateObstacleRequest request) {
        User reporter = null;
        if (reporterEmail != null) {
            reporter = userRepository.findByEmail(reporterEmail).orElse(null);
        }

        Facility facility = facilityRepository.findById(request.getFacilityId())
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found: " + request.getFacilityId()));

        LocationNode node = null;
        if (request.getLocationNodeId() != null) {
            node = nodeRepository.findById(request.getLocationNodeId()).orElse(null);
        }

        LocationEdge edge = null;
        if (request.getLocationEdgeId() != null) {
            edge = edgeRepository.findById(request.getLocationEdgeId()).orElse(null);
            if (edge != null) {
                // Immediately set edge status to BLOCKED for immediate graph rerouting
                edge.setStatus("BLOCKED");
                edgeRepository.save(edge);
            }
        }

        Obstacle obstacle = new Obstacle();
        obstacle.setFacility(facility);
        obstacle.setLocationNode(node);
        obstacle.setLocationEdge(edge);
        obstacle.setObstacleType(request.getObstacleType());
        obstacle.setDescription(request.getDescription());
        obstacle.setSeverity(request.getSeverity() != null ? request.getSeverity().toUpperCase() : "MEDIUM");
        obstacle.setStatus("ACTIVE");
        obstacle.setReportedBy(reporter);
        obstacle.setLatitude(request.getLatitude());
        obstacle.setLongitude(request.getLongitude());
        obstacle.setPhotoUrl(request.getPhotoUrl());

        if (request.getDurationMinutes() != null && request.getDurationMinutes() > 0) {
            obstacle.setExpiresAt(LocalDateTime.now().plusMinutes(request.getDurationMinutes()));
        } else {
            // Default expiry: 4 hours to prevent permanent stale blockage
            obstacle.setExpiresAt(LocalDateTime.now().plusHours(4));
        }

        Obstacle saved = obstacleRepository.save(obstacle);
        return ObstacleDto.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ObstacleDto> getFacilityObstacles(Long facilityId) {
        return obstacleRepository.findByFacilityIdOrderByCreatedAtDesc(facilityId).stream()
                .map(ObstacleDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ObstacleDto> getActiveObstacles() {
        return obstacleRepository.findByStatusOrderByCreatedAtDesc("ACTIVE").stream()
                .map(ObstacleDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ObstacleDto resolveObstacle(Long obstacleId, String resolverEmail) {
        Obstacle obstacle = obstacleRepository.findById(obstacleId)
                .orElseThrow(() -> new ResourceNotFoundException("Obstacle not found with ID: " + obstacleId));

        obstacle.setStatus("RESOLVED");
        obstacle.setResolvedAt(LocalDateTime.now());

        // Reopen edge if attached
        if (obstacle.getLocationEdge() != null) {
            LocationEdge edge = obstacle.getLocationEdge();
            edge.setStatus("OPEN");
            edgeRepository.save(edge);
        }

        Obstacle saved = obstacleRepository.save(obstacle);
        return ObstacleDto.fromEntity(saved);
    }
}
