package com.antarmarg.dto.response;

import com.antarmarg.entity.Delivery;
import com.antarmarg.entity.DeliveryStatus;

import java.time.LocalDateTime;

public class DeliveryDto {
    private Long id;
    private String trackingNumber;
    private Long customerId;
    private String customerName;
    private Long deliveryPartnerId;
    private String deliveryPartnerName;
    private Long facilityId;
    private String facilityName;
    private Long destinationNodeId;
    private String destinationNodeName;
    private DeliveryStatus status;
    private String recipientName;
    private String recipientPhone;
    private String deliveryDescription;
    private String landmark;
    private String accessInstructions;
    private String preferredTimeWindow;
    private Long recommendedGateId;
    private String recommendedGateName;
    private Double totalDistanceMeters;
    private Integer estimatedTimeSeconds;
    private Integer actualDurationSeconds;
    private LocalDateTime createdAt;
    private LocalDateTime assignedAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime startedAt;
    private LocalDateTime arrivedAt;
    private LocalDateTime completedAt;

    public DeliveryDto() {
    }

    public static DeliveryDto fromEntity(Delivery delivery) {
        if (delivery == null) return null;
        DeliveryDto dto = new DeliveryDto();
        dto.setId(delivery.getId());
        dto.setTrackingNumber(delivery.getTrackingNumber());
        dto.setCustomerId(delivery.getCustomer() != null ? delivery.getCustomer().getId() : null);
        dto.setCustomerName(delivery.getCustomer() != null ? delivery.getCustomer().getName() : null);
        dto.setDeliveryPartnerId(delivery.getDeliveryPartner() != null ? delivery.getDeliveryPartner().getId() : null);
        dto.setDeliveryPartnerName(delivery.getDeliveryPartner() != null ? delivery.getDeliveryPartner().getName() : null);
        dto.setFacilityId(delivery.getFacility() != null ? delivery.getFacility().getId() : null);
        dto.setFacilityName(delivery.getFacility() != null ? delivery.getFacility().getName() : null);
        dto.setDestinationNodeId(delivery.getDestinationNode() != null ? delivery.getDestinationNode().getId() : null);
        dto.setDestinationNodeName(delivery.getDestinationNode() != null ? delivery.getDestinationNode().getName() : null);
        dto.setStatus(delivery.getStatus());
        dto.setRecipientName(delivery.getRecipientName());
        dto.setRecipientPhone(delivery.getRecipientPhone());
        dto.setDeliveryDescription(delivery.getDeliveryDescription());
        dto.setLandmark(delivery.getLandmark());
        dto.setAccessInstructions(delivery.getAccessInstructions());
        dto.setPreferredTimeWindow(delivery.getPreferredTimeWindow());
        dto.setRecommendedGateId(delivery.getRecommendedGate() != null ? delivery.getRecommendedGate().getId() : null);
        dto.setRecommendedGateName(delivery.getRecommendedGate() != null ? delivery.getRecommendedGate().getName() : null);
        dto.setTotalDistanceMeters(delivery.getTotalDistanceMeters());
        dto.setEstimatedTimeSeconds(delivery.getEstimatedTimeSeconds());
        dto.setActualDurationSeconds(delivery.getActualDurationSeconds());
        dto.setCreatedAt(delivery.getCreatedAt());
        dto.setAssignedAt(delivery.getAssignedAt());
        dto.setAcceptedAt(delivery.getAcceptedAt());
        dto.setStartedAt(delivery.getStartedAt());
        dto.setArrivedAt(delivery.getArrivedAt());
        dto.setCompletedAt(delivery.getCompletedAt());
        return dto;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getDeliveryPartnerId() {
        return deliveryPartnerId;
    }

    public void setDeliveryPartnerId(Long deliveryPartnerId) {
        this.deliveryPartnerId = deliveryPartnerId;
    }

    public String getDeliveryPartnerName() {
        return deliveryPartnerName;
    }

    public void setDeliveryPartnerName(String deliveryPartnerName) {
        this.deliveryPartnerName = deliveryPartnerName;
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

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public String getDeliveryDescription() {
        return deliveryDescription;
    }

    public void setDeliveryDescription(String deliveryDescription) {
        this.deliveryDescription = deliveryDescription;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getAccessInstructions() {
        return accessInstructions;
    }

    public void setAccessInstructions(String accessInstructions) {
        this.accessInstructions = accessInstructions;
    }

    public String getPreferredTimeWindow() {
        return preferredTimeWindow;
    }

    public void setPreferredTimeWindow(String preferredTimeWindow) {
        this.preferredTimeWindow = preferredTimeWindow;
    }

    public Long getRecommendedGateId() {
        return recommendedGateId;
    }

    public void setRecommendedGateId(Long recommendedGateId) {
        this.recommendedGateId = recommendedGateId;
    }

    public String getRecommendedGateName() {
        return recommendedGateName;
    }

    public void setRecommendedGateName(String recommendedGateName) {
        this.recommendedGateName = recommendedGateName;
    }

    public Double getTotalDistanceMeters() {
        return totalDistanceMeters;
    }

    public void setTotalDistanceMeters(Double totalDistanceMeters) {
        this.totalDistanceMeters = totalDistanceMeters;
    }

    public Integer getEstimatedTimeSeconds() {
        return estimatedTimeSeconds;
    }

    public void setEstimatedTimeSeconds(Integer estimatedTimeSeconds) {
        this.estimatedTimeSeconds = estimatedTimeSeconds;
    }

    public Integer getActualDurationSeconds() {
        return actualDurationSeconds;
    }

    public void setActualDurationSeconds(Integer actualDurationSeconds) {
        this.actualDurationSeconds = actualDurationSeconds;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public LocalDateTime getAcceptedAt() {
        return acceptedAt;
    }

    public void setAcceptedAt(LocalDateTime acceptedAt) {
        this.acceptedAt = acceptedAt;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getArrivedAt() {
        return arrivedAt;
    }

    public void setArrivedAt(LocalDateTime arrivedAt) {
        this.arrivedAt = arrivedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
