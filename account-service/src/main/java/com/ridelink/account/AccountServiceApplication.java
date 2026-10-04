package com.ridelink.account;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.ridelink.account.entity.Account;
import com.ridelink.account.entity.AccountRole;
import com.ridelink.account.entity.AccountStatus;
import com.ridelink.account.repository.AccountRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@SpringBootApplication
public class AccountServiceApplication {

    @Bean
    OpenAPI accountServiceOpenAPI() {
        String schemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info().title("Account Service API").version("1.0"))
                .components(new Components().addSecuritySchemes(schemeName,
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }

    @Bean
    CommandLineRunner initAdmin(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminEmail = "admin@ridelink.com";
            
            if (accountRepository.findByEmail(adminEmail).isEmpty()) {
                Account admin = new Account();
                admin.setEmail(adminEmail);
                admin.setPasswordHash(passwordEncoder.encode("Admin@123")); 
                admin.setFullName("System Admin");
                admin.setPhoneNumber("0000000000");
                admin.setRole(AccountRole.ADMIN);
                admin.setStatus(AccountStatus.ACTIVE);
                accountRepository.save(admin);
                System.out.println(">>> ADMIN ACCOUNT CREATED SUCCESSFULLY! <<<");
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }
}