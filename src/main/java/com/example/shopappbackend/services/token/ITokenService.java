package com.example.shopappbackend.services.token;

import com.example.shopappbackend.entities.Token;
import com.example.shopappbackend.entities.User;
import com.example.shopappbackend.exceptions.DataNotFoundException;
import com.example.shopappbackend.exceptions.ExpiredTokenException;

public interface ITokenService {
    Token addToken(User user, String token, boolean isMobile);
    Token refreshToken(String refreshToken, User user) throws DataNotFoundException, ExpiredTokenException;
}
