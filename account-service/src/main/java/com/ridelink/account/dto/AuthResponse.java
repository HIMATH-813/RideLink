package com.ridelink.account.dto;

import java.time.Instant;

public record AuthResponse(String accessToken, String tokenType, Instant expiresAt, AccountResponse account) {
}