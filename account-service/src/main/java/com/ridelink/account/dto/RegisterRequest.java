package com.ridelink.account.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Email @Size(max = 254) String email,
        @NotBlank @Size(min = 8, max = 72) String password,
        @NotBlank @Size(max = 100) String fullName,
        @Pattern(regexp = "^$|\\+?[0-9 .()\\-]{7,20}", message = "must be a valid phone number") String phoneNumber,
        @NotBlank @Pattern(regexp = "PASSENGER|DRIVER", message = "must be PASSENGER or DRIVER") String role) {
}