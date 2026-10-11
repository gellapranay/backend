package com.antarmarg.dto.request;

import com.antarmarg.entity.NodeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateNodeRequest {

    private Long parentNodeId;

    @NotBlank(message = "Node name is required")
    private String name;

    @NotNull(message = "Node type is required")
    private NodeType nodeType;

    private Integer levelNumber = 0;
    private Double latitude;
    private Double longitude;
    private Double floorPlanX;
    private Double floorPlanY;
    private String metadataJson;

    public CreateNodeRequest() {
    }

    public Long getParentNodeId() {
        return parentNodeId;
    }

    public void setParentNodeId(Long parentNodeId) {
        this.parentNodeId = parentNodeId;
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
}
