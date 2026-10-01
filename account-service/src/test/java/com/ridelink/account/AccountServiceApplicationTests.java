package com.ridelink.account;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.boot.test.context.SpringBootTest;

import com.ridelink.account.repository.AccountRepository;

@SpringBootTest
@ActiveProfiles("test")
class AccountServiceApplicationTests {
	@MockitoBean
	AccountRepository accountRepository;

	@Test
	void contextLoads() {
	}

}
