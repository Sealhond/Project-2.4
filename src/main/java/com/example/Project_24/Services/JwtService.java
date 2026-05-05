package com.example.Project_24.Services;

import com.example.Project_24.Models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    //should be in application.properties
    private final String SECRET_KEY = "your-secret-key-that-is-long-enough-for-hs256";

    //is called after login, generates token with user info inse
    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail()) //-> who the token belongs to
                .claim("userId", user.getId()) //extra data to store inside
                .claim("role", user.getRole().name()) // same^
                .issuedAt(new Date()) //when created
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) //expire date
                .signWith(getSigningKey())
                .compact();
    }

    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    public boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
}
