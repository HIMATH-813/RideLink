package com.ridelink.account.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ridelink.account.dto.AccountResponse;
import com.ridelink.account.dto.UpdateProfileRequest;
import com.ridelink.account.service.AccountService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/accounts")
@SecurityRequirement(name = "bearerAuth")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/me")
    public AccountResponse getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return accountService.getProfile(userDetails.getUsername());
    }

    @PutMapping("/me")
    public AccountResponse updateMyProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateProfileRequest request) {
        return accountService.updateProfile(userDetails.getUsername(), request);
    }
@GetMapping("/user/{email}")
    public AccountResponse getUserDetailsByEmail(@PathVariable String email) {
        return accountService.getProfile(email);
    }
   
    @GetMapping("/{accountId}")
    public AccountResponse getUserDetailsById(@PathVariable String accountId) {
        return accountService.getProfileById(accountId);
    }
}