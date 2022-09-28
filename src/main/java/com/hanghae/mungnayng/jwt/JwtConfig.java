package com.hanghae.mungnayng.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtProvider jwtProvider;

    @Override
    public void configure(HttpSecurity httpSecurity) {
        /* 필터 객체 생성 */
        JwtFilter customJwtFilter = new JwtFilter(jwtProvider);
        JwtExceptionFilter jwtExceptionFilter = new JwtExceptionFilter();
        /* usernamepassword 필터보다 먼저 실행. */
        httpSecurity.addFilterBefore(customJwtFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterBefore(jwtExceptionFilter, JwtFilter.class);
    }
}
