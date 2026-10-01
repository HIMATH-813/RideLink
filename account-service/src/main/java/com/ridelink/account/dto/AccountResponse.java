package com.ridelink.account.dto;

import java.time.Instant;

import com.ridelink.account.entity.AccountRole;
import com.ridelink.account.entity.AccountStatus;

public record AccountResponse(
        String id,
        String email,
        String fullName,
        String phoneNumber,
        AccountRole role,
        AccountStatus status,
        Instant createdAt,
        Instant updatedAt) {
}