package com.antarmarg.dto.response;

import java.util.Map;

public class AnalyticsOverviewDto {
    private long totalFacilities;
    private long totalUsers;
    private long totalCustomers;
    private long totalDeliveryPartners;
    private long totalDeliveries;
    private long completedDeliveries;
    private long activeDeliveries;
    private long failedDeliveries;
    private long activeObstacles;
    private Double averageDurationSeconds;
    private Double averageDistanceMeters;
    private Double completionSuccessRate;
    private Map<String, Long> deliveriesByStatus;
    private Map<String, Long> obstaclesByType;

    public AnalyticsOverviewDto() {
    }

    public long getTotalFacilities() {
        return totalFacilities;
    }

    public void setTotalFacilities(long totalFacilities) {
        this.totalFacilities = totalFacilities;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public long getTotalDeliveryPartners() {
        return totalDeliveryPartners;
    }

    public void setTotalDeliveryPartners(long totalDeliveryPartners) {
        this.totalDeliveryPartners = totalDeliveryPartners;
    }

    public long getTotalDeliveries() {
        return totalDeliveries;
    }

    public void setTotalDeliveries(long totalDeliveries) {
        this.totalDeliveries = totalDeliveries;
    }

    public long getCompletedDeliveries() {
        return completedDeliveries;
    }

    public void setCompletedDeliveries(long completedDeliveries) {
        this.completedDeliveries = completedDeliveries;
    }

    public long getActiveDeliveries() {
        return activeDeliveries;
    }

    public void setActiveDeliveries(long activeDeliveries) {
        this.activeDeliveries = activeDeliveries;
    }

    public long getFailedDeliveries() {
        return failedDeliveries;
    }

    public void setFailedDeliveries(long failedDeliveries) {
        this.failedDeliveries = failedDeliveries;
    }

    public long getActiveObstacles() {
        return activeObstacles;
    }

    public void setActiveObstacles(long activeObstacles) {
        this.activeObstacles = activeObstacles;
    }

    public Double getAverageDurationSeconds() {
        return averageDurationSeconds;
    }

    public void setAverageDurationSeconds(Double averageDurationSeconds) {
        this.averageDurationSeconds = averageDurationSeconds;
    }

    public Double getAverageDistanceMeters() {
        return averageDistanceMeters;
    }

    public void setAverageDistanceMeters(Double averageDistanceMeters) {
        this.averageDistanceMeters = averageDistanceMeters;
    }

    public Double getCompletionSuccessRate() {
        return completionSuccessRate;
    }

    public void setCompletionSuccessRate(Double completionSuccessRate) {
        this.completionSuccessRate = completionSuccessRate;
    }

    public Map<String, Long> getDeliveriesByStatus() {
        return deliveriesByStatus;
    }

    public void setDeliveriesByStatus(Map<String, Long> deliveriesByStatus) {
        this.deliveriesByStatus = deliveriesByStatus;
    }

    public Map<String, Long> getObstaclesByType() {
        return obstaclesByType;
    }

    public void setObstaclesByType(Map<String, Long> obstaclesByType) {
        this.obstaclesByType = obstaclesByType;
    }
}
