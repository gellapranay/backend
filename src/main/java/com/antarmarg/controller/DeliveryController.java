package com.antarmarg.controller;

import com.antarmarg.dto.request.CreateDeliveryRequest;
import com.antarmarg.dto.request.FeedbackRequest;
import com.antarmarg.dto.response.ApiResponse;
import com.antarmarg.dto.response.DeliveryDto;
import com.antarmarg.service.DeliveryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DeliveryDto>> createDelivery(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CreateDeliveryRequest request) {
        DeliveryDto created = deliveryService.createDelivery(userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.ok("Delivery created successfully", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DeliveryDto>>> getMyDeliveries(
            @AuthenticationPrincipal UserDetails userDetails) {
        // If partner, return partner deliveries; if customer, return customer deliveries; if admin, return all
        boolean isPartner = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DELIVERY_PARTNER"));
        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SUPER_ADMIN"));

        List<DeliveryDto> list;
        if (isAdmin) {
            list = deliveryService.getAllDeliveries();
        } else if (isPartner) {
            list = deliveryService.getPartnerDeliveries(userDetails.getUsername());
        } else {
            list = deliveryService.getCustomerDeliveries(userDetails.getUsername());
        }
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @GetMapping("/available")
    @PreAuthorize("hasAnyRole('DELIVERY_PARTNER', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<DeliveryDto>>> getAvailableDeliveries() {
        List<DeliveryDto> list = deliveryService.getAvailableDeliveries();
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DeliveryDto>> getDeliveryById(@PathVariable Long id) {
        DeliveryDto delivery = deliveryService.getDeliveryById(id);
        return ResponseEntity.ok(ApiResponse.ok(delivery));
    }

    @GetMapping("/tracking/{trackingNumber}")
    public ResponseEntity<ApiResponse<DeliveryDto>> getDeliveryByTracking(@PathVariable String trackingNumber) {
        DeliveryDto delivery = deliveryService.getDeliveryByTracking(trackingNumber);
        return ResponseEntity.ok(ApiResponse.ok(delivery));
    }

    @PostMapping("/{id}/accept")
    @PreAuthorize("hasAnyRole('DELIVERY_PARTNER', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<DeliveryDto>> acceptDelivery(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        DeliveryDto updated = deliveryService.acceptDelivery(id, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok("Delivery accepted successfully", updated));
    }

    @PostMapping("/{id}/start")
    @PreAuthorize("hasAnyRole('DELIVERY_PARTNER', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<DeliveryDto>> startDelivery(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        DeliveryDto updated = deliveryService.startDelivery(id, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok("Delivery started; last-meter navigation active", updated));
    }

    @PostMapping("/{id}/arrive")
    @PreAuthorize("hasAnyRole('DELIVERY_PARTNER', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<DeliveryDto>> arriveDelivery(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        DeliveryDto updated = deliveryService.arriveDelivery(id, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok("Arrived at facility destination", updated));
    }

    @PostMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('DELIVERY_PARTNER', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<DeliveryDto>> completeDelivery(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        DeliveryDto updated = deliveryService.completeDelivery(id, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok("Delivery completed successfully", updated));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<DeliveryDto>> cancelDelivery(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody(required = false) Map<String, String> payload) {
        String reason = payload != null ? payload.getOrDefault("reason", "Cancelled by user") : "Cancelled";
        DeliveryDto updated = deliveryService.cancelDelivery(id, userDetails.getUsername(), reason);
        return ResponseEntity.ok(ApiResponse.ok("Delivery cancelled", updated));
    }

    @PostMapping("/{id}/feedback")
    public ResponseEntity<ApiResponse<String>> submitFeedback(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody FeedbackRequest request) {
        deliveryService.submitFeedback(id, userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.ok("Thank you! Route feedback recorded successfully", "OK"));
    }
}
