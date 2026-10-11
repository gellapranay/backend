package com.antarmarg.controller;

import com.antarmarg.dto.request.CreateEdgeRequest;
import com.antarmarg.dto.request.CreateFacilityRequest;
import com.antarmarg.dto.request.CreateNodeRequest;
import com.antarmarg.dto.response.ApiResponse;
import com.antarmarg.dto.response.EdgeDto;
import com.antarmarg.dto.response.FacilityDto;
import com.antarmarg.dto.response.NodeDto;
import com.antarmarg.service.FacilityService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/facilities")
public class FacilityController {

    private final FacilityService facilityService;

    public FacilityController(FacilityService facilityService) {
        this.facilityService = facilityService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FacilityDto>>> getAllFacilities() {
        List<FacilityDto> facilities = facilityService.getAllFacilities();
        return ResponseEntity.ok(ApiResponse.ok(facilities));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FacilityDto>> getFacilityById(@PathVariable Long id) {
        FacilityDto facility = facilityService.getFacilityById(id);
        return ResponseEntity.ok(ApiResponse.ok(facility));
    }

    @GetMapping("/{id}/structure")
    public ResponseEntity<ApiResponse<FacilityDto>> getFacilityStructure(@PathVariable Long id) {
        FacilityDto facility = facilityService.getFacilityStructure(id);
        return ResponseEntity.ok(ApiResponse.ok(facility));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('FACILITY_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<FacilityDto>> createFacility(@Valid @RequestBody CreateFacilityRequest request) {
        FacilityDto created = facilityService.createFacility(request);
        return ResponseEntity.ok(ApiResponse.ok("Facility created successfully", created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('FACILITY_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<FacilityDto>> updateFacility(@PathVariable Long id, @Valid @RequestBody CreateFacilityRequest request) {
        FacilityDto updated = facilityService.updateFacility(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Facility updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('FACILITY_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteFacility(@PathVariable Long id) {
        facilityService.deleteFacility(id);
        return ResponseEntity.ok(ApiResponse.ok("Facility deleted successfully", null));
    }

    // Node Endpoints
    @PostMapping("/{id}/nodes")
    @PreAuthorize("hasAnyRole('FACILITY_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<NodeDto>> addNode(@PathVariable Long id, @Valid @RequestBody CreateNodeRequest request) {
        NodeDto createdNode = facilityService.addNode(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Location node created successfully", createdNode));
    }

    @PutMapping("/{id}/nodes/{nodeId}")
    @PreAuthorize("hasAnyRole('FACILITY_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<NodeDto>> updateNode(
            @PathVariable Long id,
            @PathVariable Long nodeId,
            @Valid @RequestBody CreateNodeRequest request) {
        NodeDto updated = facilityService.updateNode(id, nodeId, request);
        return ResponseEntity.ok(ApiResponse.ok("Location node updated successfully", updated));
    }

    @DeleteMapping("/{id}/nodes/{nodeId}")
    @PreAuthorize("hasAnyRole('FACILITY_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteNode(@PathVariable Long id, @PathVariable Long nodeId) {
        facilityService.deleteNode(id, nodeId);
        return ResponseEntity.ok(ApiResponse.ok("Location node removed successfully", null));
    }

    // Edge Endpoints
    @PostMapping("/{id}/edges")
    @PreAuthorize("hasAnyRole('FACILITY_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<EdgeDto>> addEdge(@PathVariable Long id, @Valid @RequestBody CreateEdgeRequest request) {
        EdgeDto createdEdge = facilityService.addEdge(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Location edge connection created successfully", createdEdge));
    }

    @PutMapping("/{id}/edges/{edgeId}")
    @PreAuthorize("hasAnyRole('FACILITY_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<EdgeDto>> updateEdge(
            @PathVariable Long id,
            @PathVariable Long edgeId,
            @Valid @RequestBody CreateEdgeRequest request) {
        EdgeDto updated = facilityService.updateEdge(id, edgeId, request);
        return ResponseEntity.ok(ApiResponse.ok("Location edge connection updated successfully", updated));
    }

    @PatchMapping("/{id}/edges/{edgeId}/status")
    @PreAuthorize("hasAnyRole('FACILITY_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<EdgeDto>> toggleEdgeStatus(
            @PathVariable Long id,
            @PathVariable Long edgeId,
            @RequestBody Map<String, String> payload) {
        String status = payload.getOrDefault("status", "OPEN");
        EdgeDto updated = facilityService.toggleEdgeStatus(id, edgeId, status);
        return ResponseEntity.ok(ApiResponse.ok("Edge status updated to " + status, updated));
    }

    @DeleteMapping("/{id}/edges/{edgeId}")
    @PreAuthorize("hasAnyRole('FACILITY_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteEdge(@PathVariable Long id, @PathVariable Long edgeId) {
        facilityService.deleteEdge(id, edgeId);
        return ResponseEntity.ok(ApiResponse.ok("Location edge connection removed successfully", null));
    }
}
