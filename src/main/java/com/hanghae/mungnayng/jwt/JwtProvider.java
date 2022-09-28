package com.hanghae.mungnayng.jwt;

import com.hanghae.mungnayng.domain.member.Member;
import com.hanghae.mungnayng.domain.refreshToken.RefreshToken;
import com.hanghae.mungnayng.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {
    private final Key key;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserDetailsService userDetailsService;

    public JwtProvider(@Value("${jwt.secret-key}") String SECRET_KEY,
                       RefreshTokenRepository refreshTokenRepository,
                       UserDetailsService userDetailsService) {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.refreshTokenRepository = refreshTokenRepository;
        this.userDetailsService = userDetailsService;
    }

    public String createAuthorizationToken(String memberEmail, String roles) {
        Long tokenInvailedTime = 1000L * 60 * 60; /* 60m */
        return this.createToken(memberEmail, roles, tokenInvailedTime);
    }

    public String createRefreshToken(Member member, String roles) {
        Long tokenInvailedTime = 1000L * 60 * 60 * 24; /* 1d */
        String refreshToken = this.createToken(member.getEmail(), roles, tokenInvailedTime);

        RefreshToken refreshTokenObject = refreshTokenRepository.findByMember(member)
                .orElse(RefreshToken.builder()
                        .member(member)
                        .build());
        refreshTokenObject.updateTokenValue(refreshToken);

        refreshTokenRepository.save(refreshTokenObject);

        return refreshToken;
    }

    public String createToken(String memberEmail, String roles, Long tokenInvailedTime) {
        Claims claims = Jwts.claims().setSubject(memberEmail); /* claims 생성 및 payload 설정 */
        claims.put("roles", roles); /* 권한 설정, key/ value 쌍으로 저장 */
        Date date = new Date();
        return Jwts.builder()
                .setClaims(claims) /* 발행 유저 정보 저장 */
                .setIssuedAt(date) /* 발행 시간 저장 */
                .setExpiration(new Date(date.getTime() + tokenInvailedTime)) /* 토큰 유효 시간 저장 */
                .signWith(key, SignatureAlgorithm.HS256) /* 해싱 알고리즘 및 키 설정 */
                .compact(); /* 생성 */
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
            /* TODO : 토큰 자동 재발급 처리 필요, 그전까지 Exception 제외 */
//        } catch (ExpiredJwtException e) {
//            log.info("Expired JWT token, 만료된 JWT token 입니다.");
//            /* TODO : 토큰 재발급 요청 처리 추가 필요 */
//        } catch (UnsupportedJwtException e) {
//            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        String memberEmail = claims.getSubject();

        UserDetails principal = userDetailsService.loadUserByUsername(memberEmail);

        return new UsernamePasswordAuthenticationToken(principal, "", null);
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
