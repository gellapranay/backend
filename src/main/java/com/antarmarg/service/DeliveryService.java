package com.antarmarg.service;

import com.antarmarg.dto.request.CreateDeliveryRequest;
import com.antarmarg.dto.request.FeedbackRequest;
import com.antarmarg.dto.response.DeliveryDto;

import java.util.List;

public interface DeliveryService {
    DeliveryDto createDelivery(String customerEmail, CreateDeliveryRequest request);
    DeliveryDto getDeliveryById(Long id);
    DeliveryDto getDeliveryByTracking(String trackingNumber);
    List<DeliveryDto> getCustomerDeliveries(String customerEmail);
    List<DeliveryDto> getAvailableDeliveries();
    List<DeliveryDto> getPartnerDeliveries(String partnerEmail);
    List<DeliveryDto> getAllDeliveries();

    DeliveryDto acceptDelivery(Long id, String partnerEmail);
    DeliveryDto startDelivery(Long id, String partnerEmail);
    DeliveryDto arriveDelivery(Long id, String partnerEmail);
    DeliveryDto completeDelivery(Long id, String partnerEmail);
    DeliveryDto cancelDelivery(Long id, String userEmail, String reason);

    void submitFeedback(Long deliveryId, String userEmail, FeedbackRequest request);
}
