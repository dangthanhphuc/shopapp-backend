package com.example.shopappbackend.services.token;

import com.example.shopappbackend.components.JwtTokenUtils;
import com.example.shopappbackend.entities.Token;
import com.example.shopappbackend.entities.User;
import com.example.shopappbackend.exceptions.DataNotFoundException;
import com.example.shopappbackend.exceptions.ExpiredTokenException;
import com.example.shopappbackend.repositories.TokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.spi.Tokens;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TokenService implements ITokenService{
    private final TokenRepository tokenRepository;
    private final JwtTokenUtils jwtTokenUtils;

    private static final int MAX_TOKENS = 3;

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.expiration-refresh-token}")
    private long expirationRefreshToken;

    @Transactional
    @Override
    public Token addToken(User user, String token, boolean isMobile) {
        // Không cần kiêểm tra exist vì userService.getUserDetailsFromToken(token) đã làm rồi
        List<Token> tokens = tokenRepository.findByUserId(user.getId());
        int tokenCount = tokens.size();
        if(tokenCount >= MAX_TOKENS){
            List<Token> hasNonMobileToken = tokens.stream().filter(tk -> !tk.isMobile())
                    .toList();
            if(hasNonMobileToken.isEmpty()){
                tokenRepository.delete(tokens.get(0));
            } else{
                tokenRepository.delete(hasNonMobileToken.get(0));
            }
        }

        LocalDateTime expirationDate = LocalDateTime.now().plusSeconds(expiration);
        Token newToken = Token.builder()
                .token(token)
                .tokenType("Bearer")
                .refreshToken(UUID.randomUUID().toString())
                .expirationDate(LocalDateTime.now().plusSeconds(expirationRefreshToken))
                .revoked(false)
                .expired(false)
                .expirationDate(expirationDate)
                .user(user)
                .build();

        return tokenRepository.save(newToken);
    }

    @Override
    public Token refreshToken(String refreshToken, User user) throws DataNotFoundException, ExpiredTokenException {
        Token existingToken = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new DataNotFoundException("Refresh token not found"));

        if(existingToken.getExpirationDate().isBefore(LocalDateTime.now())){
            throw new ExpiredTokenException("Refresh token expired");
        }

        String newToken = jwtTokenUtils.generateToken(user);

        existingToken.setToken(newToken);
        existingToken.setExpirationDate(LocalDateTime.now().plusSeconds(expiration));
        existingToken.setRefreshToken(UUID.randomUUID().toString());
        existingToken.setRefreshTokenExpirationDate(LocalDateTime.now().plusSeconds(expirationRefreshToken));

        return tokenRepository.save(existingToken);
    }
}
