package com.example.shopappbackend.responses.user;

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

    // User details
    private Long id;
    private String username;
    private List<String> roles;
}
