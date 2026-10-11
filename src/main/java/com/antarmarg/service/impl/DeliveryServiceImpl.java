package com.antarmarg.service.impl;

import com.antarmarg.dto.request.CreateDeliveryRequest;
import com.antarmarg.dto.request.FeedbackRequest;
import com.antarmarg.dto.request.RouteRecommendationRequest;
import com.antarmarg.dto.response.DeliveryDto;
import com.antarmarg.dto.response.RouteCalculationResponse;
import com.antarmarg.entity.*;
import com.antarmarg.exception.BadRequestException;
import com.antarmarg.exception.ResourceNotFoundException;
import com.antarmarg.exception.UnauthorizedException;
import com.antarmarg.repository.*;
import com.antarmarg.routing.RouteCalculationService;
import com.antarmarg.service.DeliveryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;
    private final LocationNodeRepository nodeRepository;
    private final RouteFeedbackRepository feedbackRepository;
    private final RouteCalculationService routeCalculationService;

    public DeliveryServiceImpl(
            DeliveryRepository deliveryRepository,
            UserRepository userRepository,
            FacilityRepository facilityRepository,
            LocationNodeRepository nodeRepository,
            RouteFeedbackRepository feedbackRepository,
            RouteCalculationService routeCalculationService) {
        this.deliveryRepository = deliveryRepository;
        this.userRepository = userRepository;
        this.facilityRepository = facilityRepository;
        this.nodeRepository = nodeRepository;
        this.feedbackRepository = feedbackRepository;
        this.routeCalculationService = routeCalculationService;
    }

    @Override
    @Transactional
    public DeliveryDto createDelivery(String customerEmail, CreateDeliveryRequest request) {
        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + customerEmail));

        Facility facility = facilityRepository.findById(request.getFacilityId())
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found: " + request.getFacilityId()));

        LocationNode destinationNode = nodeRepository.findById(request.getDestinationNodeId())
                .orElseThrow(() -> new ResourceNotFoundException("Destination node not found: " + request.getDestinationNodeId()));

        if (!destinationNode.getFacility().getId().equals(facility.getId())) {
            throw new BadRequestException("Selected destination does not belong to the selected facility.");
        }

        Delivery delivery = new Delivery();
        delivery.setTrackingNumber("LMI-" + System.currentTimeMillis() % 1000000 + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase());
        delivery.setCustomer(customer);
        delivery.setFacility(facility);
        delivery.setDestinationNode(destinationNode);
        delivery.setStatus(DeliveryStatus.CREATED);
        delivery.setRecipientName(request.getRecipientName().trim());
        delivery.setRecipientPhone(request.getRecipientPhone().trim());
        delivery.setDeliveryDescription(request.getDeliveryDescription());
        delivery.setLandmark(request.getLandmark());
        delivery.setAccessInstructions(request.getAccessInstructions());
        delivery.setPreferredTimeWindow(request.getPreferredTimeWindow());

        // Calculate initial recommended route to populate gate, distance, and time
        try {
            RouteRecommendationRequest routeReq = new RouteRecommendationRequest();
            routeReq.setFacilityId(facility.getId());
            routeReq.setDestinationNodeId(destinationNode.getId());
            RouteCalculationResponse routeResp = routeCalculationService.calculateRecommendedRoute(routeReq);
            if (routeResp.isRouteFound() && routeResp.getRecommendedGate() != null) {
                LocationNode gate = nodeRepository.findById(routeResp.getRecommendedGate().getId()).orElse(null);
                delivery.setRecommendedGate(gate);
                delivery.setTotalDistanceMeters(routeResp.getTotalDistanceMeters());
                delivery.setEstimatedTimeSeconds(routeResp.getEstimatedTimeSeconds());
            }
        } catch (Exception e) {
            // Non-fatal for delivery creation
        }

        Delivery saved = deliveryRepository.save(delivery);
        return DeliveryDto.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryDto getDeliveryById(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with ID: " + id));
        return DeliveryDto.fromEntity(delivery);
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryDto getDeliveryByTracking(String trackingNumber) {
        Delivery delivery = deliveryRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with tracking number: " + trackingNumber));
        return DeliveryDto.fromEntity(delivery);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryDto> getCustomerDeliveries(String customerEmail) {
        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        return deliveryRepository.findByCustomerIdOrderByCreatedAtDesc(customer.getId()).stream()
                .map(DeliveryDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryDto> getAvailableDeliveries() {
        return deliveryRepository.findByStatusOrderByCreatedAtDesc(DeliveryStatus.CREATED).stream()
                .map(DeliveryDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryDto> getPartnerDeliveries(String partnerEmail) {
        User partner = userRepository.findByEmail(partnerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery partner not found"));
        return deliveryRepository.findByDeliveryPartnerIdOrderByCreatedAtDesc(partner.getId()).stream()
                .map(DeliveryDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryDto> getAllDeliveries() {
        return deliveryRepository.findAll().stream()
                .map(DeliveryDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DeliveryDto acceptDelivery(Long id, String partnerEmail) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found: " + id));

        User partner = userRepository.findByEmail(partnerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery partner not found"));

        if (delivery.getStatus() != DeliveryStatus.CREATED) {
            throw new BadRequestException("Delivery is no longer available (current status: " + delivery.getStatus() + ")");
        }

        delivery.setDeliveryPartner(partner);
        delivery.setStatus(DeliveryStatus.ACCEPTED);
        delivery.setAssignedAt(LocalDateTime.now());
        delivery.setAcceptedAt(LocalDateTime.now());

        Delivery saved = deliveryRepository.save(delivery);
        return DeliveryDto.fromEntity(saved);
    }

    @Override
    @Transactional
    public DeliveryDto startDelivery(Long id, String partnerEmail) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found: " + id));

        validatePartnerAccess(delivery, partnerEmail);

        if (delivery.getStatus() != DeliveryStatus.ACCEPTED) {
            throw new BadRequestException("Cannot start delivery in state: " + delivery.getStatus());
        }

        delivery.setStatus(DeliveryStatus.IN_PROGRESS);
        delivery.setStartedAt(LocalDateTime.now());

        Delivery saved = deliveryRepository.save(delivery);
        return DeliveryDto.fromEntity(saved);
    }

    @Override
    @Transactional
    public DeliveryDto arriveDelivery(Long id, String partnerEmail) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found: " + id));

        validatePartnerAccess(delivery, partnerEmail);

        if (delivery.getStatus() != DeliveryStatus.IN_PROGRESS) {
            throw new BadRequestException("Cannot mark arrival for delivery in state: " + delivery.getStatus());
        }

        delivery.setStatus(DeliveryStatus.ARRIVED);
        delivery.setArrivedAt(LocalDateTime.now());

        Delivery saved = deliveryRepository.save(delivery);
        return DeliveryDto.fromEntity(saved);
    }

    @Override
    @Transactional
    public DeliveryDto completeDelivery(Long id, String partnerEmail) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found: " + id));

        validatePartnerAccess(delivery, partnerEmail);

        if (delivery.getStatus() != DeliveryStatus.ARRIVED && delivery.getStatus() != DeliveryStatus.IN_PROGRESS) {
            throw new BadRequestException("Cannot complete delivery in state: " + delivery.getStatus());
        }

        LocalDateTime now = LocalDateTime.now();
        delivery.setStatus(DeliveryStatus.COMPLETED);
        delivery.setCompletedAt(now);

        if (delivery.getStartedAt() != null) {
            long durationSec = Duration.between(delivery.getStartedAt(), now).getSeconds();
            delivery.setActualDurationSeconds((int) durationSec);
        } else if (delivery.getAcceptedAt() != null) {
            long durationSec = Duration.between(delivery.getAcceptedAt(), now).getSeconds();
            delivery.setActualDurationSeconds((int) durationSec);
        } else {
            delivery.setActualDurationSeconds(300); // sensible default
        }

        Delivery saved = deliveryRepository.save(delivery);
        return DeliveryDto.fromEntity(saved);
    }

    @Override
    @Transactional
    public DeliveryDto cancelDelivery(Long id, String userEmail, String reason) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found: " + id));

        if (delivery.getStatus() == DeliveryStatus.COMPLETED) {
            throw new BadRequestException("Completed delivery cannot be cancelled.");
        }

        delivery.setStatus(DeliveryStatus.CANCELLED);
        Delivery saved = deliveryRepository.save(delivery);
        return DeliveryDto.fromEntity(saved);
    }

    @Override
    @Transactional
    public void submitFeedback(Long deliveryId, String userEmail, FeedbackRequest request) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found: " + deliveryId));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        RouteFeedback feedback = new RouteFeedback();
        feedback.setDelivery(delivery);
        feedback.setUser(user);
        feedback.setRating(request.getRating());
        feedback.setComment(request.getComment());
        feedback.setDifficultyScore(request.getDifficultyScore());
        feedback.setWasAccurate(request.getWasAccurate() != null ? request.getWasAccurate() : true);

        feedbackRepository.save(feedback);
    }

    private void validatePartnerAccess(Delivery delivery, String partnerEmail) {
        if (delivery.getDeliveryPartner() == null || !delivery.getDeliveryPartner().getEmail().equalsIgnoreCase(partnerEmail)) {
            throw new UnauthorizedException("You are not the assigned delivery partner for this delivery.");
        }
    }
}
