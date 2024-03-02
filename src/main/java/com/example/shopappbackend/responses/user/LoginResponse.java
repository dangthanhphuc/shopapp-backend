package com.example.shopappbackend.responses.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String message;
    private String token;
    private String tokenType;

    @JsonProperty("refresh_token")
    private String refreshToken;

    // User details
    private Long id;
    private String username;
    private List<String> roles;
}
