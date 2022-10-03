package com.hanghae.mungnayng.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SignupRequestDto {
    private String email;
    private String nickname;
    private String password;
    private String passwordConfirm;
}
