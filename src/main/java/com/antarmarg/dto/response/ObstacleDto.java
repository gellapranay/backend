package com.antarmarg.dto.response;

import com.antarmarg.entity.Obstacle;
import com.antarmarg.entity.ObstacleType;

import java.time.LocalDateTime;

public class ObstacleDto {
    private Long id;
    private Long facilityId;
    private String facilityName;
    private Long locationNodeId;
    private String locationNodeName;
    private Long locationEdgeId;
    private ObstacleType obstacleType;
    private String description;
    private String severity;
    private String status;
    private Long reportedById;
    private String reportedByName;
    private Double latitude;
    private Double longitude;
    private String photoUrl;
    private LocalDateTime expiresAt;
    private LocalDateTime resolvedAt;
    private LocalDateTime createdAt;

    public ObstacleDto() {
    }

    public static ObstacleDto fromEntity(Obstacle obstacle) {
        if (obstacle == null) return null;
        ObstacleDto dto = new ObstacleDto();
        dto.setId(obstacle.getId());
        dto.setFacilityId(obstacle.getFacility() != null ? obstacle.getFacility().getId() : null);
        dto.setFacilityName(obstacle.getFacility() != null ? obstacle.getFacility().getName() : null);
        dto.setLocationNodeId(obstacle.getLocationNode() != null ? obstacle.getLocationNode().getId() : null);
        dto.setLocationNodeName(obstacle.getLocationNode() != null ? obstacle.getLocationNode().getName() : null);
        dto.setLocationEdgeId(obstacle.getLocationEdge() != null ? obstacle.getLocationEdge().getId() : null);
        dto.setObstacleType(obstacle.getObstacleType());
        dto.setDescription(obstacle.getDescription());
        dto.setSeverity(obstacle.getSeverity());
        dto.setStatus(obstacle.getStatus());
        dto.setReportedById(obstacle.getReportedBy() != null ? obstacle.getReportedBy().getId() : null);
        dto.setReportedByName(obstacle.getReportedBy() != null ? obstacle.getReportedBy().getName() : null);
        dto.setLatitude(obstacle.getLatitude());
        dto.setLongitude(obstacle.getLongitude());
        dto.setPhotoUrl(obstacle.getPhotoUrl());
        dto.setExpiresAt(obstacle.getExpiresAt());
        dto.setResolvedAt(obstacle.getResolvedAt());
        dto.setCreatedAt(obstacle.getCreatedAt());
        return dto;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Long facilityId) {
        this.facilityId = facilityId;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public Long getLocationNodeId() {
        return locationNodeId;
    }

    public void setLocationNodeId(Long locationNodeId) {
        this.locationNodeId = locationNodeId;
    }

    public String getLocationNodeName() {
        return locationNodeName;
    }

    public void setLocationNodeName(String locationNodeName) {
        this.locationNodeName = locationNodeName;
    }

    public Long getLocationEdgeId() {
        return locationEdgeId;
    }

    public void setLocationEdgeId(Long locationEdgeId) {
        this.locationEdgeId = locationEdgeId;
    }

    public ObstacleType getObstacleType() {
        return obstacleType;
    }

    public void setObstacleType(ObstacleType obstacleType) {
        this.obstacleType = obstacleType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getReportedById() {
        return reportedById;
    }

    public void setReportedById(Long reportedById) {
        this.reportedById = reportedById;
    }

    public String getReportedByName() {
        return reportedByName;
    }

    public void setReportedByName(String reportedByName) {
        this.reportedByName = reportedByName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
