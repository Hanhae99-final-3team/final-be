package com.hanghae.mungnayng.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponseDto {
    private String nickname;
    private Long memberId;
    private boolean success;
}
