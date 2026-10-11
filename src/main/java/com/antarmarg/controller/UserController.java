package com.antarmarg.controller;

import com.antarmarg.dto.response.ApiResponse;
import com.antarmarg.dto.response.UserDto;
import com.antarmarg.entity.User;
import com.antarmarg.exception.ResourceNotFoundException;
import com.antarmarg.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserDto>> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Map<String, String> payload) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (payload.containsKey("name")) user.setName(payload.get("name").trim());
        if (payload.containsKey("phone")) user.setPhone(payload.get("phone").trim());
        if (payload.containsKey("vehicleType")) user.setVehicleType(payload.get("vehicleType"));
        if (payload.containsKey("vehicleNumber")) user.setVehicleNumber(payload.get("vehicleNumber"));
        if (payload.containsKey("serviceArea")) user.setServiceArea(payload.get("serviceArea"));
        if (payload.containsKey("organization")) user.setOrganization(payload.get("organization"));

        User saved = userRepository.save(user);
        return ResponseEntity.ok(ApiResponse.ok("Profile updated successfully", UserDto.fromEntity(saved)));
    }

    @PutMapping("/password")
    public ResponseEntity<ApiResponse<String>> updatePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Map<String, String> payload) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String newPassword = payload.get("newPassword");
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return ResponseEntity.ok(ApiResponse.ok("Password changed successfully", "OK"));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        List<UserDto> users = userRepository.findAll().stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok(users));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<UserDto>> approveUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        user.setIsApproved(true);
        User saved = userRepository.save(user);
        return ResponseEntity.ok(ApiResponse.ok("User approved successfully", UserDto.fromEntity(saved)));
    }
}
