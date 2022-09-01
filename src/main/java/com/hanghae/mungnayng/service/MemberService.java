package com.hanghae.mungnayng.service;

import com.hanghae.mungnayng.domain.member.Member;
import com.hanghae.mungnayng.domain.member.dto.SignupRequestDto;
import com.hanghae.mungnayng.exception.BadRequestException;
import com.hanghae.mungnayng.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignupRequestDto signupRequestDto) {
        checkEmailIsDuplicate(signupRequestDto.getEmail());
        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());
        Member newMember = Member.of(signupRequestDto.getEmail(), signupRequestDto.getNickname(), encodedPassword);
        memberRepository.save(newMember);
    }

    public void checkEmailIsDuplicate(String email) {
        boolean isDuplicate = memberRepository.existsByEmail(email);
        if (isDuplicate) {
            throw new BadRequestException("이미 존재하는 회원입니다.");
        }
    }
}
