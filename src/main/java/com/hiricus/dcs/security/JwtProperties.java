package com.hiricus.dcs.security;

import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtProperties {
    public static final String SECRET_KEY = "Bardoon1337Sasoon1488AndreyGayYaTozhe";
    public static final long EXPIRATION_TIME = 86400000;  // one day in ms
}
