package com.ridelink.account.service;

import java.time.Instant;
import java.util.Locale;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ridelink.account.dto.AccountResponse;
import com.ridelink.account.dto.AuthResponse;
import com.ridelink.account.dto.LoginRequest;
import com.ridelink.account.entity.Account;
import com.ridelink.account.exception.AccountNotFoundException;
import com.ridelink.account.repository.AccountRepository;
import com.ridelink.account.security.JwtTokenService;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final AccountRepository accountRepository;
    private final JwtTokenService jwtTokenService;

    public AuthenticationService(AuthenticationManager authenticationManager, AccountRepository accountRepository,
            JwtTokenService jwtTokenService) {
        this.authenticationManager = authenticationManager;
        this.accountRepository = accountRepository;
        this.jwtTokenService = jwtTokenService;
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email().trim().toLowerCase(Locale.ROOT), request.password()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Account account = accountRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(AccountNotFoundException::new);
        String token = jwtTokenService.generateToken(account);
        AccountResponse accountResponse = new AccountResponse(
                account.getId(), account.getEmail(), account.getFullName(), account.getPhoneNumber(),
                account.getRole(), account.getStatus(), account.getCreatedAt(), account.getUpdatedAt());
        return new AuthResponse(token, "Bearer", jwtTokenService.getExpirationTime(), accountResponse);
    }
}