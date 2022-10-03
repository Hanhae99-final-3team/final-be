package com.hanghae.mungnayng.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(request, response);
        } catch (ExpiredJwtException exception) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (JwtException exception) {
            log.warn(exception.getMessage(), exception);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } catch (IOException exception) {
            log.warn(exception.getMessage(), exception);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
