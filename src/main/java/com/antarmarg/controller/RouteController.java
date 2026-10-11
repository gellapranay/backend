package com.antarmarg.controller;

import com.antarmarg.dto.request.RouteRecommendationRequest;
import com.antarmarg.dto.response.ApiResponse;
import com.antarmarg.dto.response.RouteCalculationResponse;
import com.antarmarg.entity.Delivery;
import com.antarmarg.exception.ResourceNotFoundException;
import com.antarmarg.repository.DeliveryRepository;
import com.antarmarg.routing.RouteCalculationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteCalculationService routeCalculationService;
    private final DeliveryRepository deliveryRepository;

    public RouteController(RouteCalculationService routeCalculationService, DeliveryRepository deliveryRepository) {
        this.routeCalculationService = routeCalculationService;
        this.deliveryRepository = deliveryRepository;
    }

    @PostMapping("/recommend")
    public ResponseEntity<ApiResponse<RouteCalculationResponse>> recommendRoute(@Valid @RequestBody RouteRecommendationRequest request) {
        RouteCalculationResponse response = routeCalculationService.calculateRecommendedRoute(request);
        if (!response.isRouteFound()) {
            return ResponseEntity.ok(ApiResponse.error(response.getMessage()));
        }
        return ResponseEntity.ok(ApiResponse.ok("Optimal route calculated", response));
    }

    @PostMapping("/recalculate/{deliveryId}")
    public ResponseEntity<ApiResponse<RouteCalculationResponse>> recalculateDeliveryRoute(
            @PathVariable Long deliveryId,
            @RequestParam(required = false, defaultValue = "false") Boolean requireAccessible) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found: " + deliveryId));

        RouteRecommendationRequest request = new RouteRecommendationRequest();
        request.setFacilityId(delivery.getFacility().getId());
        request.setDestinationNodeId(delivery.getDestinationNode().getId());
        request.setRequireAccessible(requireAccessible);

        RouteCalculationResponse response = routeCalculationService.calculateRecommendedRoute(request);
        return ResponseEntity.ok(ApiResponse.ok("Route recalculated dynamically", response));
    }
}
