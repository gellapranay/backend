package com.antarmarg.dto.response;

import java.util.ArrayList;
import java.util.List;

public class RouteCalculationResponse {
    private boolean routeFound;
    private String message;
    private NodeDto recommendedGate;
    private List<NodeDto> pathNodes = new ArrayList<>();
    private List<EdgeDto> pathEdges = new ArrayList<>();
    private Double totalDistanceMeters;
    private Integer estimatedTimeSeconds;
    private Boolean isAccessible;
    private List<RouteInstructionDto> instructions = new ArrayList<>();
    private List<String> obstaclesAvoided = new ArrayList<>();
    private String recommendationRationale;
    private List<RouteCalculationResponse> alternativeRoutes = new ArrayList<>();

    public RouteCalculationResponse() {
    }

    public static RouteCalculationResponse notFound(String message) {
        RouteCalculationResponse res = new RouteCalculationResponse();
        res.setRouteFound(false);
        res.setMessage(message);
        return res;
    }

    // Getters and Setters
    public boolean isRouteFound() {
        return routeFound;
    }

    public void setRouteFound(boolean routeFound) {
        this.routeFound = routeFound;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NodeDto getRecommendedGate() {
        return recommendedGate;
    }

    public void setRecommendedGate(NodeDto recommendedGate) {
        this.recommendedGate = recommendedGate;
    }

    public List<NodeDto> getPathNodes() {
        return pathNodes;
    }

    public void setPathNodes(List<NodeDto> pathNodes) {
        this.pathNodes = pathNodes;
    }

    public List<EdgeDto> getPathEdges() {
        return pathEdges;
    }

    public void setPathEdges(List<EdgeDto> pathEdges) {
        this.pathEdges = pathEdges;
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

    public Boolean getIsAccessible() {
        return isAccessible;
    }

    public void setIsAccessible(Boolean accessible) {
        isAccessible = accessible;
    }

    public List<RouteInstructionDto> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<RouteInstructionDto> instructions) {
        this.instructions = instructions;
    }

    public List<String> getObstaclesAvoided() {
        return obstaclesAvoided;
    }

    public void setObstaclesAvoided(List<String> obstaclesAvoided) {
        this.obstaclesAvoided = obstaclesAvoided;
    }

    public String getRecommendationRationale() {
        return recommendationRationale;
    }

    public void setRecommendationRationale(String recommendationRationale) {
        this.recommendationRationale = recommendationRationale;
    }

    public List<RouteCalculationResponse> getAlternativeRoutes() {
        return alternativeRoutes;
    }

    public void setAlternativeRoutes(List<RouteCalculationResponse> alternativeRoutes) {
        this.alternativeRoutes = alternativeRoutes;
    }
}
