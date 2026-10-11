package com.antarmarg.dto.request;

import com.antarmarg.entity.ObstacleType;
import jakarta.validation.constraints.NotNull;

public class CreateObstacleRequest {

    @NotNull(message = "Facility ID is required")
    private Long facilityId;

    private Long locationNodeId;
    private Long locationEdgeId;

    @NotNull(message = "Obstacle type is required")
    private ObstacleType obstacleType;

    private String description;
    private String severity = "MEDIUM";
    private Double latitude;
    private Double longitude;
    private String photoUrl;
    private Integer durationMinutes; // optional auto-expiry

    public CreateObstacleRequest() {
    }

    public Long getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Long facilityId) {
        this.facilityId = facilityId;
    }

    public Long getLocationNodeId() {
        return locationNodeId;
    }

    public void setLocationNodeId(Long locationNodeId) {
        this.locationNodeId = locationNodeId;
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

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
}
