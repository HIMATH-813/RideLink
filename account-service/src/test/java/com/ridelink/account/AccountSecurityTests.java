package com.ridelink.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.ridelink.account.entity.Account;
import com.ridelink.account.entity.AccountRole;
import com.ridelink.account.entity.AccountStatus;
import com.ridelink.account.security.AccountPrincipal;
import com.ridelink.account.security.JwtTokenService;

class AccountSecurityTests {
    private static final String JWT_SECRET = "test-secret-key-that-is-long-enough-for-hmac-signing";

    @Test
    void suspendedAccountIsDisabledAndReceivesItsRoleAuthority() {
        Account account = createAccount(AccountStatus.SUSPENDED);

        AccountPrincipal principal = new AccountPrincipal(account);

        assertThat(principal.isEnabled()).isFalse();
        assertThat(principal.getAuthorities()).extracting("authority").containsExactly("ROLE_DRIVER");
    }

    @Test
    void generatedTokenContainsTheAccountEmailAndIsValid() {
        Account account = createAccount(AccountStatus.ACTIVE);
        AccountPrincipal principal = new AccountPrincipal(account);
        JwtTokenService tokenService = new JwtTokenService(JWT_SECRET, 900_000);

        String token = tokenService.generateToken(account);

        assertThat(tokenService.extractUsername(token)).isEqualTo(account.getEmail());
        assertThat(tokenService.isTokenValid(token, principal)).isTrue();
    }

    @Test
    void constructorRejectsShortSigningSecrets() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new JwtTokenService("short-secret", 900_000))
                .withMessage("security.jwt.secret must contain at least 32 UTF-8 bytes");
    }

    @Test
    void constructorRejectsNonPositiveExpiration() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new JwtTokenService(JWT_SECRET, 0))
                .withMessage("security.jwt.expiration-ms must be greater than zero");
    }

    private Account createAccount(AccountStatus status) {
        Instant now = Instant.now();
        return new Account("driver@example.com", "hashed-password", "RideLink Driver", null,
                AccountRole.DRIVER, status, now, now);
    }
}