package com.hanghae.mungnayng.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KakaoUserInfoDto {
    private Long KakaoId;
    private String nickname;
    private String email;
}
