package com.hanghae.mungnayng.repository;

import com.hanghae.mungnayng.domain.member.Member;
import com.hanghae.mungnayng.domain.refreshToken.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByMember(Member member);

    Optional<RefreshToken> findByMember(Member member);

    Optional<RefreshToken> findByTokenValue(String tokenValue);
}
