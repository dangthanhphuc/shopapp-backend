package com.example.shopappbackend.services.token;

import com.example.shopappbackend.entities.Token;
import com.example.shopappbackend.entities.User;

public interface ITokenService {
    Token addToken(User user, String token, boolean isMobile);
}
