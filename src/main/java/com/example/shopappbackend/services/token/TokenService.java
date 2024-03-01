package com.example.shopappbackend.services.token;

import com.example.shopappbackend.entities.Token;
import com.example.shopappbackend.entities.User;
import com.example.shopappbackend.repositories.TokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.spi.Tokens;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TokenService implements ITokenService{
    private final TokenRepository tokenRepository;

    private static final int MAX_TOKENS = 3;

    @Value("${jwt.expiration}")
    private long expiration;

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
                .isMobile(isMobile)
                .revoked(false)
                .expired(false)
                .expirationDate(expirationDate)
                .user(user)
                .build();

        return tokenRepository.save(newToken);
    }
}
