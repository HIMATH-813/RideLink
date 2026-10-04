package com.ridelink.account;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.ridelink.account.dto.AccountResponse;
import com.ridelink.account.dto.UpdateAccountStatusRequest;
import com.ridelink.account.entity.AccountRole;
import com.ridelink.account.entity.AccountStatus;
import com.ridelink.account.repository.AccountRepository;
import com.ridelink.account.service.AccountService;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AccountServiceApplicationTests {
	@Autowired
	MockMvc mockMvc;

	@MockitoBean
	AccountRepository accountRepository;

	@MockitoBean
	AccountService accountService;

	@Test
	void contextLoads() {
	}

	@Test
	@WithMockUser(roles = "PASSENGER")
	void nonAdminCannotUpdateAccountStatus() throws Exception {
		mockMvc.perform(put("/api/v1/admin/accounts/person@example.com/status")
					.contentType(MediaType.APPLICATION_JSON)
					.content("{\"status\":\"INACTIVE\"}"))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void adminCanUpdateAccountStatusByEmail() throws Exception {
		Instant now = Instant.now();
		when(accountService.updateStatusByEmail(eq("person@example.com"), any(UpdateAccountStatusRequest.class)))
				.thenReturn(new AccountResponse("account-id", "person@example.com", "Person", null,
						AccountRole.PASSENGER, AccountStatus.INACTIVE, now, now));

		mockMvc.perform(put("/api/v1/admin/accounts/person@example.com/status")
					.contentType(MediaType.APPLICATION_JSON)
					.content("{\"status\":\"INACTIVE\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("person@example.com"))
				.andExpect(jsonPath("$.status").value("INACTIVE"));
	}

}
