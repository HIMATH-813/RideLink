package com.ridelink.account.service;

import com.ridelink.account.dto.AccountResponse;
import com.ridelink.account.dto.RegisterRequest;
import com.ridelink.account.dto.UpdateAccountStatusRequest;
import com.ridelink.account.dto.UpdateProfileRequest;

public interface AccountService {
    AccountResponse register(RegisterRequest request);

    AccountResponse getProfile(String email);

    AccountResponse updateProfile(String email, UpdateProfileRequest request);

    AccountResponse updateStatusByEmail(String email, UpdateAccountStatusRequest request);

    AccountResponse getProfileById(String accountId);
}