package com.hanghae.mungnayng.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae.mungnayng.domain.UserDetailsImpl;
import com.hanghae.mungnayng.domain.member.Member;
import com.hanghae.mungnayng.domain.member.dto.KakaoUserInfoDto;
import com.hanghae.mungnayng.domain.member.dto.LoginResponseDto;
import com.hanghae.mungnayng.jwt.JwtProvider;
import com.hanghae.mungnayng.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KakaoService {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    String RedirectURI;

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public LoginResponseDto kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {

        /* "인가 코드"로 "액세스 토큰" 요청 */
        String accessToken = getKakaoAccessToken(code);

        /* 토큰으로 카카오 API 호출 */
        KakaoUserInfoDto kakaoUserInfoDto = getKakaoUserInfo(accessToken);

        /* 카카오ID로 회원가입 처리 */
        Member kakaoUser = signupKakaoUser(kakaoUserInfoDto);

        /* 강제 로그인 처리 */
        forceLoginKakaoUser(kakaoUser);

        String authorizationToken = jwtProvider.createAuthorizationToken(kakaoUser.getEmail(), kakaoUser.getRole());
        String refreshToken = jwtProvider.createRefreshToken(kakaoUser, kakaoUser.getRole());
        tokenToHeaders(authorizationToken, refreshToken, response);

        return new LoginResponseDto(kakaoUser.getNickname(), kakaoUser.getMemberId(), true);
    }

    private String getKakaoAccessToken(String code) throws JsonProcessingException {
        /* HTTP Header 생성 */
        HttpHeaders headers;
        headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        /* HTTP Body 생성 */
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoClientId);
        body.add("redirect_uri", RedirectURI);
        body.add("code", code);

        /* HTTP 요청 보내기 */
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        /* HTTP 응답 (JSON) -> 액세스 토큰 파싱 */
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        /* HTTP Header 생성 */
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        /* HTTP 요청 보내기 */
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String email = "";
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        if (null != jsonNode.get("kakao_account").get("email")) {
            email = jsonNode.get("kakao_account")
                    .get("email").asText();
        } else {
            email = "kakao_" + id;
        }
        System.out.println("카카오 사용자 정보: " + id + ", " + nickname + ", " + email);
        return new KakaoUserInfoDto(id, nickname, email);
    }

    private Member signupKakaoUser(KakaoUserInfoDto kakaoUserInfoDto) {
        /* 재가입 방지 */
//        int mannerTemp = userRoleCheckService.userResignCheck(kakaoUserInfoDto.getEmail());
        /* DB 에 중복된 Kakao Id 가 있는지 확인 */
        Long kakaoId = kakaoUserInfoDto.getKakaoId();
        Member findKakao = memberRepository.findByKakaoId(kakaoUserInfoDto.getKakaoId())
                .orElse(null);

        /* DB에 중복된 계정이 없으면 회원가입 처리 */
        if (findKakao == null) {
            String nickName = kakaoUserInfoDto.getNickname();
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);
            String email = kakaoUserInfoDto.getEmail();
            LocalDateTime createdAt = LocalDateTime.now();
            Member kakaoUser = Member.builder()
                    .nickname(nickName)
                    .password(encodedPassword)
                    .email(email)
                    .kakaoId(kakaoId)
                    .role("ROLE_USER")
                    .build();
            memberRepository.save(kakaoUser);

            return kakaoUser;
        }
        return findKakao;
    }

    public void forceLoginKakaoUser(Member kakaoUser) {
        UserDetails userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void tokenToHeaders(String authorizationToken, String refreshToken, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + authorizationToken);
        response.addHeader("RefreshToken", refreshToken);
    }
}
