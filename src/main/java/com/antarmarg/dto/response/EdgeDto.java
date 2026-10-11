package com.antarmarg.dto.response;

import com.antarmarg.entity.LocationEdge;

public class EdgeDto {
    private Long id;
    private Long facilityId;
    private Long sourceNodeId;
    private String sourceNodeName;
    private Long destinationNodeId;
    private String destinationNodeName;
    private Double distanceMeters;
    private Integer estimatedTimeSeconds;
    private Boolean isAccessible;
    private Boolean allowVehicles;
    private Boolean isBidirectional;
    private String status;
    private Boolean isActive;

    public EdgeDto() {
    }

    public static EdgeDto fromEntity(LocationEdge edge) {
        if (edge == null) return null;
        EdgeDto dto = new EdgeDto();
        dto.setId(edge.getId());
        dto.setFacilityId(edge.getFacility() != null ? edge.getFacility().getId() : null);
        dto.setSourceNodeId(edge.getSourceNode() != null ? edge.getSourceNode().getId() : null);
        dto.setSourceNodeName(edge.getSourceNode() != null ? edge.getSourceNode().getName() : null);
        dto.setDestinationNodeId(edge.getDestinationNode() != null ? edge.getDestinationNode().getId() : null);
        dto.setDestinationNodeName(edge.getDestinationNode() != null ? edge.getDestinationNode().getName() : null);
        dto.setDistanceMeters(edge.getDistanceMeters());
        dto.setEstimatedTimeSeconds(edge.getEstimatedTimeSeconds());
        dto.setIsAccessible(edge.getIsAccessible());
        dto.setAllowVehicles(edge.getAllowVehicles());
        dto.setIsBidirectional(edge.getIsBidirectional());
        dto.setStatus(edge.getStatus());
        dto.setIsActive(edge.getIsActive());
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

    public Long getSourceNodeId() {
        return sourceNodeId;
    }

    public void setSourceNodeId(Long sourceNodeId) {
        this.sourceNodeId = sourceNodeId;
    }

    public String getSourceNodeName() {
        return sourceNodeName;
    }

    public void setSourceNodeName(String sourceNodeName) {
        this.sourceNodeName = sourceNodeName;
    }

    public Long getDestinationNodeId() {
        return destinationNodeId;
    }

    public void setDestinationNodeId(Long destinationNodeId) {
        this.destinationNodeId = destinationNodeId;
    }

    public String getDestinationNodeName() {
        return destinationNodeName;
    }

    public void setDestinationNodeName(String destinationNodeName) {
        this.destinationNodeName = destinationNodeName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }
}
