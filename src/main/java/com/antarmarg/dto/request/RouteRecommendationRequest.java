package com.antarmarg.dto.request;

import jakarta.validation.constraints.NotNull;

public class RouteRecommendationRequest {

    @NotNull(message = "Facility ID is required")
    private Long facilityId;

    private Long preferredGateId; // optional override

    @NotNull(message = "Destination node ID is required")
    private Long destinationNodeId;

    private Boolean requireAccessible = false;
    private Boolean isVehicle = false;

    public RouteRecommendationRequest() {
    }

    public Long getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Long facilityId) {
        this.facilityId = facilityId;
    }

    public Long getPreferredGateId() {
        return preferredGateId;
    }

    public void setPreferredGateId(Long preferredGateId) {
        this.preferredGateId = preferredGateId;
    }

    public Long getDestinationNodeId() {
        return destinationNodeId;
    }

    public void setDestinationNodeId(Long destinationNodeId) {
        this.destinationNodeId = destinationNodeId;
    }

    public Boolean getRequireAccessible() {
        return requireAccessible;
    }

    public void setRequireAccessible(Boolean requireAccessible) {
        this.requireAccessible = requireAccessible;
    }

    public Boolean getIsVehicle() {
        return isVehicle;
    }

    public void setIsVehicle(Boolean vehicle) {
        isVehicle = vehicle;
    }
}
