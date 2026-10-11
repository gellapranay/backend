package com.antarmarg.service;

import com.antarmarg.dto.request.CreateObstacleRequest;
import com.antarmarg.dto.response.ObstacleDto;

import java.util.List;

public interface ObstacleService {
    ObstacleDto reportObstacle(String reporterEmail, CreateObstacleRequest request);
    List<ObstacleDto> getFacilityObstacles(Long facilityId);
    List<ObstacleDto> getActiveObstacles();
    ObstacleDto resolveObstacle(Long obstacleId, String resolverEmail);
}
