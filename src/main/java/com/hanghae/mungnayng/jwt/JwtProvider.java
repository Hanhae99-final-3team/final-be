package com.hanghae.mungnayng.jwt;

import com.hanghae.mungnayng.domain.member.Member;
import com.hanghae.mungnayng.domain.refreshToken.RefreshToken;
import com.hanghae.mungnayng.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {
    private final Key key;
    private final RefreshTokenRepository refreshTokenRepository;
    private static final Long TOKEN_VALID_TIME = 1000L * 60 * 3; // 3m


    public JwtProvider(@Value("${jwt.secret-key}")String SECRET_KEY, RefreshTokenRepository refreshTokenRepository){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String createAuthorizationToken(String userId, String roles) {
        Long tokenInvailedTime = 1000L * 60 * 3; // 3m
        return this.createToken(userId, roles, tokenInvailedTime);
    }

    public String createRefreshToken(Member member, String roles) {
        Long tokenInvailedTime = 1000L * 60 * 60 * 24; // 1d
        String refreshToken = this.createToken(member.getEmail(), roles, tokenInvailedTime);

        RefreshToken refreshTokenObject = refreshTokenRepository.findByMember(member)
                .orElse(RefreshToken.builder()
                .member(member)
                .build());
        refreshTokenObject.updateTokenValue(refreshToken);

        refreshTokenRepository.save(refreshTokenObject);

        return refreshToken;
    }

    public String createToken(String userId, String roles, Long tokenInvailedTime){
        Claims claims = Jwts.claims().setSubject(userId); // claims 생성 및 payload 설정
        claims.put("roles", roles); // 권한 설정, key/ value 쌍으로 저장
        Date date = new Date();
        return Jwts.builder()
                .setClaims(claims) // 발행 유저 정보 저장
                .setIssuedAt(date) // 발행 시간 저장
                .setExpiration(new Date(date.getTime() + tokenInvailedTime)) // 토큰 유효 시간 저장
                .signWith(key, SignatureAlgorithm.HS256) // 해싱 알고리즘 및 키 설정
                .compact(); // 생성
    }
}
