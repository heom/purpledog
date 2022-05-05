package com.example.purpledog.security;

import com.example.purpledog.dto.UserDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationUtils {

    private final JwtAuthenticationProperties jwtAuthenticationProperties;

    public String createToken(UserDto userDto) {
        return Jwts.builder()
                .setSubject(userDto.getId())
                .setExpiration(new Date(System.currentTimeMillis()
                        + Long.parseLong(jwtAuthenticationProperties.getExpiration_time())))
                .signWith(SignatureAlgorithm.HS512, jwtAuthenticationProperties.getSecret())
                .compact();
    }

    public String getIdByJwtToken(String jwtToken) {
        return Jwts.parser().setSigningKey(jwtAuthenticationProperties.getSecret())
                .parseClaimsJws(jwtToken).getBody()
                .getSubject();
    }
}
