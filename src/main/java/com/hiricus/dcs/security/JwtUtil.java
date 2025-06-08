package com.hiricus.dcs.security;

import com.hiricus.dcs.model.object.user.RoleObject;
import com.hiricus.dcs.security.data.UserAuthRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Configuration
public class JwtUtil {
    private final SecretKey secretKey = Keys.hmacShaKeyFor(JwtProperties.SECRET_KEY.getBytes());

    public String generateToken(String login, List<String> userRoles) {
        return Jwts.builder()
                .setClaims(Map.of("login", login, "roles", userRoles))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String encrypt(String data) {
        return Base64.getEncoder().encodeToString(data.getBytes());
    }

    public String decrypt(String token) {
        return new String(Base64.getDecoder().decode(token));
    }
}
