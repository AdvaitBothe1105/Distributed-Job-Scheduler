package com.jobscheduler.job_service.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {
    @Value("${app.jwt.secret}")
    private String secret;

    // only need this — extract email from token
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignerKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // and this — validate token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignerKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private Key getSignerKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
