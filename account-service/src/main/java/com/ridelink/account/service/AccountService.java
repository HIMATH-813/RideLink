package com.ridelink.account.service;

import com.ridelink.account.dto.AccountResponse;
import com.ridelink.account.dto.RegisterRequest;
import com.ridelink.account.dto.UpdateProfileRequest;
import com.ridelink.account.dto.UpdateAccountStatusRequest;

public interface AccountService {
    AccountResponse register(RegisterRequest request);

    AccountResponse getProfile(String email);

    AccountResponse updateProfile(String email, UpdateProfileRequest request);

    AccountResponse updateStatus(String accountId, UpdateAccountStatusRequest request);
}