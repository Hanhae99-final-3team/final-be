package com.hanghae.mungnayng.repository;

import com.hanghae.mungnayng.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByKakaoId(Long kakaoId);
    Member findByNickname(String nickname);
}
