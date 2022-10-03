package com.hanghae.mungnayng.exception;

import com.hanghae.mungnayng.domain.member.dto.SignupResponseDto;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public SignupResponseDto handleBadRequestException(BadRequestException exception) {
        return new SignupResponseDto(exception.getMessage(), false);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<?> handleUnsupportedJwtException(UnsupportedJwtException exception) {
        String errorMessage = exception.getMessage();
        log.warn(errorMessage, exception.getCause());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("success", false, "msg", errorMessage));
    }
}
