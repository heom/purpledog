package com.example.purpledog.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("token")
@Data
public class JwtAuthenticationProperties {
    private String expiration_time;
    private String secret;
}
