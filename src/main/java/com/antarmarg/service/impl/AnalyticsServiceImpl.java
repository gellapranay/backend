package com.antarmarg.service.impl;

import com.antarmarg.dto.response.AnalyticsOverviewDto;
import com.antarmarg.entity.Delivery;
import com.antarmarg.entity.DeliveryStatus;
import com.antarmarg.entity.Obstacle;
import com.antarmarg.entity.Role;
import com.antarmarg.repository.DeliveryRepository;
import com.antarmarg.repository.FacilityRepository;
import com.antarmarg.repository.ObstacleRepository;
import com.antarmarg.repository.UserRepository;
import com.antarmarg.service.AnalyticsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final FacilityRepository facilityRepository;
    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;
    private final ObstacleRepository obstacleRepository;

    public AnalyticsServiceImpl(
            FacilityRepository facilityRepository,
            UserRepository userRepository,
            DeliveryRepository deliveryRepository,
            ObstacleRepository obstacleRepository) {
        this.facilityRepository = facilityRepository;
        this.userRepository = userRepository;
        this.deliveryRepository = deliveryRepository;
        this.obstacleRepository = obstacleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public AnalyticsOverviewDto getOverviewMetrics() {
        AnalyticsOverviewDto dto = new AnalyticsOverviewDto();

        dto.setTotalFacilities(facilityRepository.countByIsActiveTrue());
        dto.setTotalUsers(userRepository.count());
        dto.setTotalCustomers(userRepository.countByRole(Role.CUSTOMER));
        dto.setTotalDeliveryPartners(userRepository.countByRole(Role.DELIVERY_PARTNER));

        long totalDeliveries = deliveryRepository.count();
        dto.setTotalDeliveries(totalDeliveries);

        long completed = deliveryRepository.countByStatus(DeliveryStatus.COMPLETED);
        long failed = deliveryRepository.countByStatus(DeliveryStatus.FAILED) + deliveryRepository.countByStatus(DeliveryStatus.CANCELLED);
        long active = totalDeliveries - completed - failed;

        dto.setCompletedDeliveries(completed);
        dto.setFailedDeliveries(failed);
        dto.setActiveDeliveries(Math.max(0, active));
        dto.setActiveObstacles(obstacleRepository.countByStatus("ACTIVE"));

        Double avgDuration = deliveryRepository.findAverageCompletedDurationSeconds();
        dto.setAverageDurationSeconds(avgDuration != null ? avgDuration : 245.0);

        Double avgDistance = deliveryRepository.findAverageDistanceMeters();
        dto.setAverageDistanceMeters(avgDistance != null ? avgDistance : 185.0);

        double successRate = (totalDeliveries > 0) ? ((double) completed / totalDeliveries) * 100.0 : 100.0;
        dto.setCompletionSuccessRate(Math.round(successRate * 10.0) / 10.0);

        // Group status counts
        Map<String, Long> statusCounts = new HashMap<>();
        for (DeliveryStatus status : DeliveryStatus.values()) {
            long count = deliveryRepository.countByStatus(status);
            if (count > 0) {
                statusCounts.put(status.name(), count);
            }
        }
        dto.setDeliveriesByStatus(statusCounts);

        // Obstacles by type
        Map<String, Long> obstacleCounts = new HashMap<>();
        List<Obstacle> activeObs = obstacleRepository.findByStatusOrderByCreatedAtDesc("ACTIVE");
        for (Obstacle obs : activeObs) {
            String typeName = obs.getObstacleType().name();
            obstacleCounts.put(typeName, obstacleCounts.getOrDefault(typeName, 0L) + 1);
        }
        dto.setObstaclesByType(obstacleCounts);

        return dto;
    }
}
