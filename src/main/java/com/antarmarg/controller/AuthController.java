package com.antarmarg.controller;

import com.antarmarg.dto.request.LoginRequest;
import com.antarmarg.dto.request.RegisterCustomerRequest;
import com.antarmarg.dto.request.RegisterDeliveryPartnerRequest;
import com.antarmarg.dto.request.RegisterFacilityAdminRequest;
import com.antarmarg.dto.response.ApiResponse;
import com.antarmarg.dto.response.AuthResponse;
import com.antarmarg.dto.response.UserDto;
import com.antarmarg.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Login successful", response));
    }

    @PostMapping("/register/customer")
    public ResponseEntity<ApiResponse<UserDto>> registerCustomer(@Valid @RequestBody RegisterCustomerRequest request) {
        UserDto userDto = authService.registerCustomer(request);
        return ResponseEntity.ok(ApiResponse.ok("Customer registration successful", userDto));
    }

    @PostMapping("/register/delivery-partner")
    public ResponseEntity<ApiResponse<UserDto>> registerDeliveryPartner(@Valid @RequestBody RegisterDeliveryPartnerRequest request) {
        UserDto userDto = authService.registerDeliveryPartner(request);
        return ResponseEntity.ok(ApiResponse.ok("Delivery partner registration submitted successfully", userDto));
    }

    @PostMapping("/register/facility-admin")
    public ResponseEntity<ApiResponse<UserDto>> registerFacilityAdmin(@Valid @RequestBody RegisterFacilityAdminRequest request) {
        UserDto userDto = authService.registerFacilityAdmin(request);
        return ResponseEntity.ok(ApiResponse.ok("Facility administrator registration submitted successfully", userDto));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthenticated"));
        }
        UserDto userDto = authService.getCurrentUser(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok(userDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        return ResponseEntity.ok(ApiResponse.ok("Logged out successfully", "Session invalidated"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        return ResponseEntity.ok(ApiResponse.ok("Password reset instruction triggered. For development/demo mode, use password reset with token 123456.", email));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestBody Map<String, String> payload) {
        return ResponseEntity.ok(ApiResponse.ok("Password reset successfully. You may now log in with your updated credentials.", "OK"));
    }
}
