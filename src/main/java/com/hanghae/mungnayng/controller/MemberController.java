package com.hanghae.mungnayng.controller;

import com.hanghae.mungnayng.domain.member.dto.EmailCheckRequestDto;
import com.hanghae.mungnayng.domain.member.dto.SignupRequestDto;
import com.hanghae.mungnayng.domain.member.dto.SignupResponseDto;
import com.hanghae.mungnayng.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members/signup")
    public SignupResponseDto signup(@RequestBody SignupRequestDto signupRequestDto) {
        memberService.signUp(signupRequestDto);
        return new SignupResponseDto("회원가입 성공",true);
    }

    @GetMapping("/members/email-check")// 이메일 가입여부 검사
    public SignupResponseDto emailcheck(@RequestBody EmailCheckRequestDto emailCheckRequestDto) {

        memberService.checkEmailIsDuplicate(emailCheckRequestDto.getEmail());

        return new SignupResponseDto("가입이 가능한 회원입니다.",true);
    }

    @PostMapping("/members/login")
    public boolean login(@RequestBody SignupRequestDto signupRequestDto) {
        return true;
    }

}
