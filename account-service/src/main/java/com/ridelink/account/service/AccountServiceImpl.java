package com.ridelink.account.service;

import java.time.Instant;
import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ridelink.account.dto.AccountResponse;
import com.ridelink.account.dto.RegisterRequest;
import com.ridelink.account.dto.UpdateAccountStatusRequest;
import com.ridelink.account.dto.UpdateProfileRequest;
import com.ridelink.account.entity.Account;
import com.ridelink.account.entity.AccountRole;
import com.ridelink.account.entity.AccountStatus;
import com.ridelink.account.exception.AccountAlreadyExistsException;
import com.ridelink.account.exception.AccountNotFoundException;
import com.ridelink.account.repository.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AccountResponse register(RegisterRequest request) {
        String email = normalizeEmail(request.email());
        if (accountRepository.existsByEmail(email)) {
            throw new AccountAlreadyExistsException(email);
        }

        Instant now = Instant.now();
        Account account = new Account(
                email,
                passwordEncoder.encode(request.password()),
                request.fullName().trim(),
                normalizePhoneNumber(request.phoneNumber()),
                AccountRole.valueOf(request.role()),
                AccountStatus.ACTIVE,
                now,
                now);
        return toResponse(accountRepository.save(account));
    }

    @Override
    public AccountResponse getProfile(String email) {
        return toResponse(findByEmail(email));
    }

    @Override
    public AccountResponse updateProfile(String email, UpdateProfileRequest request) {
        Account account = findByEmail(email);
        account.setFullName(request.fullName().trim());
        if (request.phoneNumber() != null) {
            account.setPhoneNumber(normalizePhoneNumber(request.phoneNumber()));
        }
        account.setUpdatedAt(Instant.now());
        return toResponse(accountRepository.save(account));
    }

    @Override
    public AccountResponse updateStatus(String accountId, UpdateAccountStatusRequest request) {
        Account account = accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);
        account.setStatus(request.status());
        account.setUpdatedAt(Instant.now());
        return toResponse(accountRepository.save(account));
    }

    private Account findByEmail(String email) {
        return accountRepository.findByEmail(normalizeEmail(email)).orElseThrow(AccountNotFoundException::new);
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private String normalizePhoneNumber(String phoneNumber) {
        return phoneNumber == null || phoneNumber.isBlank() ? null : phoneNumber.trim();
    }

    private AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getEmail(),
                account.getFullName(),
                account.getPhoneNumber(),
                account.getRole(),
                account.getStatus(),
                account.getCreatedAt(),
                account.getUpdatedAt());
    }
}