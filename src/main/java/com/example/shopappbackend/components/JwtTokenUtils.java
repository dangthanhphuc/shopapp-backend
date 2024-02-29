package com.example.shopappbackend.components;

import com.example.shopappbackend.entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Component
@RequiredArgsConstructor
public class JwtTokenUtils {
    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.secretKey}")
    private String secretKey;

    public String generateToken(User user){
        Map<String, Object> claims = new HashMap<>();
        // Set thuộc tính không trùng đại diện (Mã tài khoản) cho đối tượng User
        claims.put("email", user.getEmail());
        claims.put("address", user.getAddress());
        claims.put("phone_number", user.getPhoneNumber());

        return Jwts.builder()
                .claims(claims) // Claims là một đối tượng chứa thông tin cụ thể được đặt vào JWT. (VD: role, permission)
                .issuer("https://localhost:8088/api/v1/") //Issuer cung cấp thông tin về địa chỉ (URL hoặc chuỗi định danh) của đơn vị tạo ra JWT. Nhằm xác định nguồn gốc của JWT và có thể hữu ích trong việc xác thực và kiểm tra tính toàn vẹn của JWT.
                .subject(user.getEmail()) // Subject dùng để biểu diễn thông tin về đối tượng chính của JWT (Mã tài khoản)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() * expiration * 1000L))
                .signWith(getSignKey()) // Key ở đây là chìa khóa dùng để giải mã JWT sau khi nó được mã hóa
                .compact();
    }

    private SecretKey getSignKey(){
        // Mã bảo mật dùng để tăng cường mật mã
        byte[] secretBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(secretBytes);
    }

    public <T> T extractClaimFromToken(String token, Function<Claims, T> claimsResolver){
        // Lấy thông tin từ claim theo claimsResolver truyền vào
        final Claims claims = extractAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }


    public Claims extractAllClaimsFromToken(String token){
        // Lấy claims lưu thông tin từ token
        return Jwts.parser()
                .verifyWith(getSignKey()) // Giải mã token với SecretKey đã dùng để tạo token
                .build()
                .parseSignedClaims(token) // Truyền token để giải mã
                .getPayload();
    }

    public String getEmailFromToken(String token){
        return extractClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationFromDate(String token){
        return extractClaimFromToken(token, Claims::getExpiration);
    }

    public String getIssuerFromToken(String token){
        return extractClaimFromToken(token, Claims::getIssuer);
    }

    public boolean isTokenExpired(String token){
        Date expiration = getExpirationFromDate(token);
        return expiration.before(new Date());
    }

    // Chưa xử lý validateToken
//    public boolean validateToken(String token, User userDetails) {
//        try {
//            String phoneNumber = extractPhoneNumber(token);
//            Token existingToken = tokenRepository.findByToken(token);
//            if(existingToken == null ||
//                    existingToken.isRevoked() == true ||
//                    !userDetails.isActive()
//            ) {
//                return false;
//            }
//            return (phoneNumber.equals(userDetails.getUsername()))
//                    && !isTokenExpired(token);
//        } catch (MalformedJwtException e) {
//            logger.error("Invalid JWT token: {}", e.getMessage());
//        } catch (ExpiredJwtException e) {
//            logger.error("JWT token is expired: {}", e.getMessage());
//        } catch (UnsupportedJwtException e) {
//            logger.error("JWT token is unsupported: {}", e.getMessage());
//        } catch (IllegalArgumentException e) {
//            logger.error("JWT claims string is empty: {}", e.getMessage());
//        }
//
//        return false;
//    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String email = getEmailFromToken(token);
        return email.equals(userDetails.getUsername()) // Email từ token có giống với email từ UserDetails không
                && !isTokenExpired(token); // Kiểm tra token còn hạn sử dụng không

    }
}
