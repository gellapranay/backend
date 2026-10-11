package com.antarmarg.service.impl;

import com.antarmarg.dto.request.LoginRequest;
import com.antarmarg.dto.request.RegisterCustomerRequest;
import com.antarmarg.dto.request.RegisterDeliveryPartnerRequest;
import com.antarmarg.dto.request.RegisterFacilityAdminRequest;
import com.antarmarg.dto.response.AuthResponse;
import com.antarmarg.dto.response.UserDto;
import com.antarmarg.entity.Role;
import com.antarmarg.entity.User;
import com.antarmarg.exception.BadRequestException;
import com.antarmarg.exception.ResourceNotFoundException;
import com.antarmarg.repository.UserRepository;
import com.antarmarg.security.JwtTokenProvider;
import com.antarmarg.security.UserPrincipal;
import com.antarmarg.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail().toLowerCase().trim(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return new AuthResponse(jwt, UserDto.fromEntity(user));
    }

    @Override
    @Transactional
    public UserDto registerCustomer(RegisterCustomerRequest request) {
        String email = request.getEmail().toLowerCase().trim();
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email already in use: " + email);
        }

        User user = new User();
        user.setName(request.getName().trim());
        user.setEmail(email);
        user.setPhone(request.getPhone().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.CUSTOMER);
        user.setIsApproved(true);
        user.setIsActive(true);

        User saved = userRepository.save(user);
        return UserDto.fromEntity(saved);
    }

    @Override
    @Transactional
    public UserDto registerDeliveryPartner(RegisterDeliveryPartnerRequest request) {
        String email = request.getEmail().toLowerCase().trim();
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email already in use: " + email);
        }

        User user = new User();
        user.setName(request.getName().trim());
        user.setEmail(email);
        user.setPhone(request.getPhone().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.DELIVERY_PARTNER);
        user.setVehicleType(request.getVehicleType());
        user.setVehicleNumber(request.getVehicleNumber());
        user.setServiceArea(request.getServiceArea());
        user.setIsApproved(true); // Auto-approved for major project demo
        user.setIsActive(true);

        User saved = userRepository.save(user);
        return UserDto.fromEntity(saved);
    }

    @Override
    @Transactional
    public UserDto registerFacilityAdmin(RegisterFacilityAdminRequest request) {
        String email = request.getEmail().toLowerCase().trim();
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email already in use: " + email);
        }

        User user = new User();
        user.setName(request.getName().trim());
        user.setEmail(email);
        user.setPhone(request.getPhone().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.FACILITY_ADMIN);
        user.setOrganization(request.getOrganization());
        user.setFacilityAccessRequest(request.getFacilityAccessRequest());
        user.setIsApproved(true);
        user.setIsActive(true);

        User saved = userRepository.save(user);
        return UserDto.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return UserDto.fromEntity(user);
    }
}
