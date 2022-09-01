package com.hanghae.mungnayng.service;

import com.hanghae.mungnayng.domain.member.Member;
import com.hanghae.mungnayng.domain.member.dto.LoginRequestDto;
import com.hanghae.mungnayng.domain.member.dto.SignupRequestDto;
import com.hanghae.mungnayng.exception.BadRequestException;
import com.hanghae.mungnayng.jwt.JwtProvider;
import com.hanghae.mungnayng.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

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

    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        Member member = memberRepository
                .findByEmail(loginRequestDto.getEmail()).orElseThrow(() -> new BadRequestException("아이디 혹은 비밀번호를 확인하세요."));
        checkPassword(loginRequestDto.getPassword(), member.getPassword());

        String token = jwtProvider.createToken(member.getEmail(), member.getRole());
        tokenToHeaders(token,response);
    }

    private void checkPassword(String password, String encodedPassword) {
        boolean isSame = passwordEncoder.matches(password, encodedPassword);
        if(!isSame) {
            throw new BadRequestException("아이디 혹은 비밀번호를 확인하세요.");
        }
    }

    public void tokenToHeaders(String token, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + token);
    }


}
