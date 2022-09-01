package com.hanghae.mungnayng.exception;

import com.hanghae.mungnayng.domain.member.dto.SignupResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public SignupResponseDto handle(BadRequestException e) {
        return new SignupResponseDto(e.getMessage(),false);
    }
}
