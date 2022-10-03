package com.hanghae.mungnayng.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanghae.mungnayng.domain.UserDetailsImpl;
import com.hanghae.mungnayng.domain.member.Member;
import com.hanghae.mungnayng.domain.member.dto.*;
import com.hanghae.mungnayng.service.KakaoService;
import com.hanghae.mungnayng.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Api(tags = {"회원 API 정보를 제공하는 Controller"})
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final KakaoService kakaoService;

    /* 회원 가입 */
    @ApiOperation(value = "회원가입 메소드")
    @PostMapping("/members/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDto signupRequestDto) {
        memberService.signUp(signupRequestDto);
        return ResponseEntity.ok(new SignupResponseDto("회원가입 성공", true));
    }

    /* 이메일 중복 검사 */
    @ApiOperation(value = "이메일 중복 검사 메소드")
    @PostMapping("/members/email-check")
    public ResponseEntity<?> emailcheck(@RequestBody EmailCheckRequestDto emailCheckRequestDto) {
        memberService.checkEmailIsDuplicate(emailCheckRequestDto.getEmail());
        return ResponseEntity.ok(new SignupResponseDto("가입이 가능한 회원입니다.", true));
    }

    /* 닉네임 중복 검사 */
    @ApiOperation(value = "닉네임 중복 검사 메소드")
    @PostMapping("/members/nickname-check")
    public ResponseEntity<?> nicknamecheck(@RequestBody NicknameCheckRequestDto nicknameCheckRequestDto) {
        memberService.checkNicknameIsDuplicate(nicknameCheckRequestDto.getNickname());
        return ResponseEntity.ok(new SignupResponseDto("사용 가능한 닉네임입니다.", true));
    }

    /* 로그인 */
    @ApiOperation(value = "로그인 메소드")
    @PostMapping("/members/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        return ResponseEntity.ok(memberService.login(loginRequestDto, response));
    }

    /* 로그 아웃 */
    @ApiOperation(value = "로그 아웃 메소드")
    @PostMapping("/members/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserDetails userDetails) {
        Member member = ((UserDetailsImpl) userDetails).getMember();
        memberService.logout(member);
        return ResponseEntity.ok(new SignupResponseDto("로그아웃 성공.", true));
    }

    /* 토큰재발행 */
    @ApiOperation(value = "토큰 재발행 메소드")
    @PostMapping("/members/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        memberService.reissue(request, response);
        return ResponseEntity.ok().body(Map.of("success", true, "msg", "토큰 재발행 성공"));
    }

    /* 카카오 로그인 */
    @ApiOperation(value = "카카오 로그인 콜백 메소드")
    @GetMapping("/members/kakao/callback")
    public ResponseEntity<?> kakaoLogin(@RequestParam(value = "code") String code, HttpServletResponse response) throws JsonProcessingException {
        return ResponseEntity.ok(kakaoService.kakaoLogin(code, response));
    }

    /* 검색어 자동저장 토글 ON/OFF */
    @ApiOperation(value = "검색어 자동저장 토글 On/Off 메소드")
    @PutMapping("/items/search/toggle")
    public ResponseEntity<?> changeToggle(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok().body(memberService.changeToggle(userDetails));
    }
}
