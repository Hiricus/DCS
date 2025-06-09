package com.hiricus.dcs.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtProperties {
    public final String SECRET_KEY;
    public final long EXPIRATION_TIME;


    public JwtProperties(@Value("${jwt.secret}") String SECRET_KEY,
                         @Value("${jwt.lifetime}") long EXPIRATION_TIME) {
        this.SECRET_KEY = SECRET_KEY;
        this.EXPIRATION_TIME = EXPIRATION_TIME;
    }


}
