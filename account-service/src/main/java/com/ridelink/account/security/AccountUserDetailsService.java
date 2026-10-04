package com.ridelink.account.security;

import java.util.Locale;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ridelink.account.entity.Account;
import com.ridelink.account.repository.AccountRepository;

@Service
public class AccountUserDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;

    public AccountUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("--- LOGIN ATTEMPT ---");
        System.out.println("Email trying to login: " + email);
        
        Account account = accountRepository.findByEmail(normalizeEmail(email))
                .orElseThrow(() -> {
                    System.out.println("Result: ACCOUNT NOT FOUND IN DB!");
                    return new UsernameNotFoundException("Account not found");
                });

        System.out.println("Result: Account found successfully!");
        System.out.println("DB Password Hash: " + account.getPasswordHash());
        
        return new AccountPrincipal(account);
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}