package com.ridelink.account.service;

import com.ridelink.account.dto.AccountResponse;
import com.ridelink.account.entity.Account;
import com.ridelink.account.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account sampleAccount;

    @BeforeEach
    void setUp() {
        sampleAccount = new Account();
        sampleAccount.setEmail("driver_kamal@example.com");
        sampleAccount.setFullName("Kamal Perera");
        sampleAccount.setPhoneNumber("0711122334");
    }

    @Test
    void testGetProfile_Success() {
        when(accountRepository.findByEmail("driver_kamal@example.com"))
                .thenReturn(Optional.of(sampleAccount));

        AccountResponse response = accountService.getProfile("driver_kamal@example.com");

        assertNotNull(response);
        assertEquals("driver_kamal@example.com", response.email());
        verify(accountRepository, times(1)).findByEmail("driver_kamal@example.com");
    }
}