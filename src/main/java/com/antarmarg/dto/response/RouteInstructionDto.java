package com.antarmarg.dto.response;

public class RouteInstructionDto {
    private int stepNumber;
    private String instruction;
    private String nodeName;
    private String nodeType;
    private Double distanceMeters;
    private Integer timeSeconds;
    private String iconType; // GATE, ROAD, LIFT, STAIRS, CORRIDOR, DESTINATION

    public RouteInstructionDto() {
    }

    public RouteInstructionDto(int stepNumber, String instruction, String nodeName, String nodeType, Double distanceMeters, Integer timeSeconds, String iconType) {
        this.stepNumber = stepNumber;
        this.instruction = instruction;
        this.nodeName = nodeName;
        this.nodeType = nodeType;
        this.distanceMeters = distanceMeters;
        this.timeSeconds = timeSeconds;
        this.iconType = iconType;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public Double getDistanceMeters() {
        return distanceMeters;
    }

    public void setDistanceMeters(Double distanceMeters) {
        this.distanceMeters = distanceMeters;
    }

    public Integer getTimeSeconds() {
        return timeSeconds;
    }

    public void setTimeSeconds(Integer timeSeconds) {
        this.timeSeconds = timeSeconds;
    }

    public String getIconType() {
        return iconType;
    }

    public void setIconType(String iconType) {
        this.iconType = iconType;
    }
}
