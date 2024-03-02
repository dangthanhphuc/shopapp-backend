package com.example.shopappbackend.repositories;

import com.example.shopappbackend.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByToken(String token);
    Optional<Token> findByRefreshToken(String refreshToken);
    List<Token> findByUserId(Long userId);
}
