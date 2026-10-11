package com.antarmarg.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "location_edges")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LocationEdge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "source_node_id", nullable = false)
    private LocationNode sourceNode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_node_id", nullable = false)
    private LocationNode destinationNode;

    @Column(name = "distance_meters", nullable = false)
    private Double distanceMeters = 10.0;

    @Column(name = "estimated_time_seconds", nullable = false)
    private Integer estimatedTimeSeconds = 15;

    @Column(name = "is_accessible")
    private Boolean isAccessible = true;

    @Column(name = "allow_vehicles")
    private Boolean allowVehicles = false;

    @Column(name = "is_bidirectional")
    private Boolean isBidirectional = true;

    @Column(nullable = false)
    private String status = "OPEN"; // OPEN, BLOCKED, RESTRICTED

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public LocationEdge() {
    }

    public LocationEdge(Long id, Facility facility, LocationNode sourceNode, LocationNode destinationNode, Double distanceMeters, Integer estimatedTimeSeconds) {
        this.id = id;
        this.facility = facility;
        this.sourceNode = sourceNode;
        this.destinationNode = destinationNode;
        this.distanceMeters = distanceMeters;
        this.estimatedTimeSeconds = estimatedTimeSeconds;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public LocationNode getSourceNode() {
        return sourceNode;
    }

    public void setSourceNode(LocationNode sourceNode) {
        this.sourceNode = sourceNode;
    }

    public LocationNode getDestinationNode() {
        return destinationNode;
    }

    public void setDestinationNode(LocationNode destinationNode) {
        this.destinationNode = destinationNode;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
