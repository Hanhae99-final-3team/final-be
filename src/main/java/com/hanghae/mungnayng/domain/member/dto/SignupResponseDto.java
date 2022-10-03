package com.hanghae.mungnayng.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignupResponseDto {
    private String msg;
    private boolean success;
}
