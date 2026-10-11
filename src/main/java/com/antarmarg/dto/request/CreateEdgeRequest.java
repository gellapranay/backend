package com.antarmarg.dto.request;

import jakarta.validation.constraints.NotNull;

public class CreateEdgeRequest {

    @NotNull(message = "Source node ID is required")
    private Long sourceNodeId;

    @NotNull(message = "Destination node ID is required")
    private Long destinationNodeId;

    private Double distanceMeters = 10.0;
    private Integer estimatedTimeSeconds = 15;
    private Boolean isAccessible = true;
    private Boolean allowVehicles = false;
    private Boolean isBidirectional = true;

    public CreateEdgeRequest() {
    }

    public Long getSourceNodeId() {
        return sourceNodeId;
    }

    public void setSourceNodeId(Long sourceNodeId) {
        this.sourceNodeId = sourceNodeId;
    }

    public Long getDestinationNodeId() {
        return destinationNodeId;
    }

    public void setDestinationNodeId(Long destinationNodeId) {
        this.destinationNodeId = destinationNodeId;
    }

    public Double getDistanceMeters() {
        return distanceMeters;
    }

    public void setDistanceMeters(Double distanceMeters) {
        this.distanceMeters = distanceMeters;
    }

    public Integer getEstimatedTimeSeconds() {
        return estimatedTimeSeconds;
    }

    public void setEstimatedTimeSeconds(Integer estimatedTimeSeconds) {
        this.estimatedTimeSeconds = estimatedTimeSeconds;
    }

    public Boolean getIsAccessible() {
        return isAccessible;
    }

    public void setIsAccessible(Boolean accessible) {
        isAccessible = accessible;
    }

    public Boolean getAllowVehicles() {
        return allowVehicles;
    }

    public void setAllowVehicles(Boolean allowVehicles) {
        this.allowVehicles = allowVehicles;
    }

    public Boolean getIsBidirectional() {
        return isBidirectional;
    }

    public void setIsBidirectional(Boolean bidirectional) {
        isBidirectional = bidirectional;
    }
}
