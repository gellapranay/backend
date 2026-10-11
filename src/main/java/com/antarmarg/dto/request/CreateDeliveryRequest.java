package com.antarmarg.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateDeliveryRequest {

    @NotNull(message = "Facility ID is required")
    private Long facilityId;

    @NotNull(message = "Destination node ID is required")
    private Long destinationNodeId;

    @NotBlank(message = "Recipient name is required")
    private String recipientName;

    @NotBlank(message = "Recipient phone is required")
    private String recipientPhone;

    private String deliveryDescription;
    private String landmark;
    private String accessInstructions;
    private String preferredTimeWindow;

    public CreateDeliveryRequest() {
    }

    public Long getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Long facilityId) {
        this.facilityId = facilityId;
    }

    public Long getDestinationNodeId() {
        return destinationNodeId;
    }

    public void setDestinationNodeId(Long destinationNodeId) {
        this.destinationNodeId = destinationNodeId;
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
}
