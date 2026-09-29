package com.ridelink.account.dto;

import com.ridelink.account.entity.AccountStatus;

import jakarta.validation.constraints.NotNull;

public record UpdateAccountStatusRequest(@NotNull AccountStatus status) {
}