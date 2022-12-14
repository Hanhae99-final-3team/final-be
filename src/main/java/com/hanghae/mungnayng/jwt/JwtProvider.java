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
        Long tokenInvailedTime = 1000L * 60 * 50; /* 60m */
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
        Claims claims = Jwts.claims().setSubject(memberEmail); /* claims ?????? ??? payload ?????? */
        claims.put("roles", roles); /* ?????? ??????, key/ value ????????? ?????? */
        Date date = new Date();
        return Jwts.builder()
                .setClaims(claims) /* ?????? ?????? ?????? ?????? */
                .setIssuedAt(date) /* ?????? ?????? ?????? */
                .setExpiration(new Date(date.getTime() + tokenInvailedTime)) /* ?????? ?????? ?????? ?????? */
                .signWith(key, SignatureAlgorithm.HS256) /* ?????? ???????????? ??? ??? ?????? */
                .compact(); /* ?????? */
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, ???????????? ?????? JWT ?????? ?????????.");
            /* TODO : ?????? ?????? ????????? ?????? ??????, ???????????? Exception ?????? */
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, ????????? JWT token ?????????.");
//            /* TODO : ?????? ????????? ?????? ?????? ?????? ?????? */
//        } catch (UnsupportedJwtException e) {
//            log.info("Unsupported JWT token, ???????????? ?????? JWT ?????? ?????????.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, ????????? JWT ?????? ?????????.");
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
