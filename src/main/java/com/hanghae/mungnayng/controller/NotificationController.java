package com.hanghae.mungnayng.controller;

import com.hanghae.mungnayng.domain.Notification.Dto.NotificationResponseDto;
import com.hanghae.mungnayng.domain.UserDetailsImpl;
import com.hanghae.mungnayng.domain.member.Member;
import com.hanghae.mungnayng.service.EmitterService;
import com.hanghae.mungnayng.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final EmitterService emitterService;

    @GetMapping("/notifications")
    public ResponseEntity<List<NotificationResponseDto>> getNotification(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        List<NotificationResponseDto> responseDtoList = notificationService.getNotification(member.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    @GetMapping("/Notifications/read")
    public ResponseEntity<String> readOk(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        return ResponseEntity.status(HttpStatus.OK).contentType(new MediaType("application", "text", StandardCharsets.UTF_8 ))
                .body(notificationService.readOK(member.getEmail()));
    }

    @DeleteMapping("/Notifications")
    public ResponseEntity<String> DeleteNotification(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        return ResponseEntity.status(HttpStatus.OK).contentType(new MediaType("application","text",StandardCharsets.UTF_8))
                .body(notificationService.deleteNotification(member.getEmail()));
    }

    @GetMapping("Subscribe/{memberId}")
    public SseEmitter subscribe(@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId,
                                @PathVariable Long memberId) {
        return emitterService.createEmitter(memberId);
    }
}
