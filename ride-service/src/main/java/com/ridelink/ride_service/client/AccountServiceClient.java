package com.ridelink.ride_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AccountServiceClient {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${account-service.base-url}")
    private String accountServiceBaseUrl;

    public String getCurrentUserId(String authorizationHeader) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, authorizationHeader);

        ResponseEntity<AccountMeResponse> response = restTemplate.exchange(
                accountServiceBaseUrl + "/api/v1/accounts/me",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                AccountMeResponse.class);

        if (response.getBody() == null || response.getBody().id() == null) {
            throw new IllegalStateException("Account service returned no user ID");
        }

        return response.getBody().id();
    }

    // Long වෙනුවට String ලෙස වෙනස් කර ඇත
    public record AccountMeResponse(String id) {}
}