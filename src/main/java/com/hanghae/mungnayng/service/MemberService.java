package com.hanghae.mungnayng.service;

import com.hanghae.mungnayng.domain.member.Member;
import com.hanghae.mungnayng.domain.member.dto.LoginRequestDto;
import com.hanghae.mungnayng.domain.member.dto.LoginResponseDto;
import com.hanghae.mungnayng.domain.member.dto.SignupRequestDto;
import com.hanghae.mungnayng.exception.BadRequestException;
import com.hanghae.mungnayng.jwt.JwtProvider;
import com.hanghae.mungnayng.repository.MemberRepository;
import com.hanghae.mungnayng.repository.RefreshTokenRepository;
import com.hanghae.mungnayng.util.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final Validator validator;

    @Transactional
    public void signUp(SignupRequestDto signupRequestDto) {
        checkEmailIsDuplicate(signupRequestDto.getEmail());
        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());
        Member newMember = new Member(signupRequestDto.getEmail(), signupRequestDto.getNickname(), encodedPassword);
        memberRepository.save(newMember);
    }

    public void checkEmailIsDuplicate(String email) {
        boolean isDuplicate = memberRepository.existsByEmail(email);
        if (isDuplicate) {
            throw new BadRequestException("이미 존재하는 회원입니다.");
        }
    }

    public void checkNicknameIsDuplicate(String nickname) {
        boolean isDuplicate = memberRepository.existsByNickname(nickname);
        if (isDuplicate) {
            throw new BadRequestException("이미 존재하는 닉네임 입니다.");
        }
    }

    @Transactional
    public LoginResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        Member member = memberRepository
                .findByEmail(loginRequestDto.getEmail()).orElseThrow(() -> new BadRequestException("아이디 혹은 비밀번호를 확인하세요."));
        checkPassword(loginRequestDto.getPassword(), member.getPassword());

        String accessToken = jwtProvider.createAuthorizationToken(member.getEmail(), member.getRole());
        String refreshToken = jwtProvider.createRefreshToken(member, member.getRole());
        tokenToHeaders(accessToken, refreshToken, response);
        return new LoginResponseDto(member.getNickname(), member.getMemberId(), true);
    }

    private void checkPassword(String password, String encodedPassword) {
        boolean isSame = passwordEncoder.matches(password, encodedPassword);
        if (!isSame) {
            throw new BadRequestException("아이디 혹은 비밀번호를 확인하세요.");
        }
    }

    public void tokenToHeaders(String authorizationToken, String refreshToken, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + authorizationToken);
        response.addHeader("RefreshToken", refreshToken);
    }

    @Transactional
    public void logout(Member member) {
        refreshTokenRepository.deleteByMember(member);
    }

    /* 검색어 자동저장 토글 On/Off */
    @Transactional
    public boolean changeToggle(UserDetails userDetails) {
        validator.validateUserDetailsInput(userDetails);   /* 로그인 유효성 검사 */

        Member member = memberRepository.findByNickname(userDetails.getUsername());
        boolean toggle = member.isToggle();
        toggle = !toggle;
        member.updateToggle(toggle);

        return toggle;
    }
}
