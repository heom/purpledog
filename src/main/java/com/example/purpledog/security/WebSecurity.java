package com.example.purpledog.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationEntryPoint entryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeRequests().antMatchers(HttpMethod.POST, "/users").permitAll()
                .and()
                .authorizeRequests().antMatchers("/login"
                        , "/h2/**" //H2 브라우저 DB
                        , "/h2-console/**" //H2 브라우저 DB
                        , "/v2/api-docs" //Swagger
                        , "/configuration/**" //Swagger
                        , "/swagger*/**" //Swagger
                        , "/webjars/**" //Swagger
                ).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterAfter(jwtAuthenticationFilter, LogoutFilter.class);

        http.headers().frameOptions().disable()
                .and()
                .exceptionHandling().authenticationEntryPoint(entryPoint);
    }
}
