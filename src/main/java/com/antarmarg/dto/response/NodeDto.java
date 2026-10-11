package com.antarmarg.dto.response;

import com.antarmarg.entity.LocationNode;
import com.antarmarg.entity.NodeType;

public class NodeDto {
    private Long id;
    private Long facilityId;
    private Long parentNodeId;
    private String parentNodeName;
    private String name;
    private NodeType nodeType;
    private Integer levelNumber;
    private Double latitude;
    private Double longitude;
    private Double floorPlanX;
    private Double floorPlanY;
    private String metadataJson;
    private Boolean isActive;

    public NodeDto() {
    }

    public static NodeDto fromEntity(LocationNode node) {
        if (node == null) return null;
        NodeDto dto = new NodeDto();
        dto.setId(node.getId());
        dto.setFacilityId(node.getFacility() != null ? node.getFacility().getId() : null);
        dto.setParentNodeId(node.getParentNode() != null ? node.getParentNode().getId() : null);
        dto.setParentNodeName(node.getParentNode() != null ? node.getParentNode().getName() : null);
        dto.setName(node.getName());
        dto.setNodeType(node.getNodeType());
        dto.setLevelNumber(node.getLevelNumber());
        dto.setLatitude(node.getLatitude());
        dto.setLongitude(node.getLongitude());
        dto.setFloorPlanX(node.getFloorPlanX());
        dto.setFloorPlanY(node.getFloorPlanY());
        dto.setMetadataJson(node.getMetadataJson());
        dto.setIsActive(node.getIsActive());
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

    public Long getParentNodeId() {
        return parentNodeId;
    }

    public void setParentNodeId(Long parentNodeId) {
        this.parentNodeId = parentNodeId;
    }

    public String getParentNodeName() {
        return parentNodeName;
    }

    public void setParentNodeName(String parentNodeName) {
        this.parentNodeName = parentNodeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public Integer getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(Integer levelNumber) {
        this.levelNumber = levelNumber;
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

    public Double getFloorPlanX() {
        return floorPlanX;
    }

    public void setFloorPlanX(Double floorPlanX) {
        this.floorPlanX = floorPlanX;
    }

    public Double getFloorPlanY() {
        return floorPlanY;
    }

    public void setFloorPlanY(Double floorPlanY) {
        this.floorPlanY = floorPlanY;
    }

    public String getMetadataJson() {
        return metadataJson;
    }

    public void setMetadataJson(String metadataJson) {
        this.metadataJson = metadataJson;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }
}
