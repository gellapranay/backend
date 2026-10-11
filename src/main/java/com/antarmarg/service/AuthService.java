package com.antarmarg.service;

import com.antarmarg.dto.request.LoginRequest;
import com.antarmarg.dto.request.RegisterCustomerRequest;
import com.antarmarg.dto.request.RegisterDeliveryPartnerRequest;
import com.antarmarg.dto.request.RegisterFacilityAdminRequest;
import com.antarmarg.dto.response.AuthResponse;
import com.antarmarg.dto.response.UserDto;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    UserDto registerCustomer(RegisterCustomerRequest request);
    UserDto registerDeliveryPartner(RegisterDeliveryPartnerRequest request);
    UserDto registerFacilityAdmin(RegisterFacilityAdminRequest request);
    UserDto getCurrentUser(String email);
}
