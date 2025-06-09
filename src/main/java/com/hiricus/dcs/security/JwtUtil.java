package com.hiricus.dcs.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Configuration
public class JwtUtil {
    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    @Autowired
    public JwtUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        secretKey = Keys.hmacShaKeyFor(jwtProperties.SECRET_KEY.getBytes());
    }

    public String generateToken(Integer id, String login, List<String> userRoles) {
//        System.out.println("Id from generate token: " + id);
        return Jwts.builder()
                .setClaims(Map.of(
                        "id", id,
                        "login", login,
                        "roles", userRoles
                ))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.EXPIRATION_TIME))
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
