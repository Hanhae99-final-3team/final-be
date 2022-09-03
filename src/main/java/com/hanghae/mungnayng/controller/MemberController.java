package com.hanghae.mungnayng.controller;

import com.hanghae.mungnayng.domain.UserDetailsImpl;
import com.hanghae.mungnayng.domain.member.Member;
import com.hanghae.mungnayng.domain.member.dto.*;
import com.hanghae.mungnayng.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    /* 회원 가입 */
    @PostMapping("/members/signup")
    public SignupResponseDto signup(@RequestBody SignupRequestDto signupRequestDto) {
        memberService.signUp(signupRequestDto);
        return new SignupResponseDto("회원가입 성공", true);
    }

    /* 이메일 중복 검사 */
    @PostMapping("/members/email-check")
    public SignupResponseDto emailcheck(@RequestBody EmailCheckRequestDto emailCheckRequestDto) {
        memberService.checkEmailIsDuplicate(emailCheckRequestDto.getEmail());
        return new SignupResponseDto("가입이 가능한 회원입니다.", true);
    }

    /* 닉네임 중복 검사 */
    @PostMapping("/members/nickname-check")
    public SignupResponseDto nicknamecheck(@RequestBody NicknameCheckRequestDto nicknameCheckRequestDto) {
        memberService.checkNicknameIsDuplicate(nicknameCheckRequestDto.getNickname());
        return new SignupResponseDto("사용 가능한 닉네임입니다.", true);
    }

    /* 로그인 */
    @PostMapping("/members/login")
    public SignupResponseDto login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        memberService.login(loginRequestDto, response);
        return new SignupResponseDto("로그인 성공.", true);
    }

    /* 로그 아웃 */
    @PostMapping("/members/logout")
    public SignupResponseDto logout(@AuthenticationPrincipal UserDetails userDetails) {
        Member member = ((UserDetailsImpl) userDetails).getMember();
        memberService.logout(member);
        return new SignupResponseDto("로그아웃 성공.", true);
    }
}
