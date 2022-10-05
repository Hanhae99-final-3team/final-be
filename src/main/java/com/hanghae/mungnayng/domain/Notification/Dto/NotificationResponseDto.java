package com.hanghae.mungnayng.domain.Notification.Dto;

import com.hanghae.mungnayng.domain.Notification.Notification;
import com.hanghae.mungnayng.domain.Notification.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationResponseDto implements Serializable {
    private Type type;
    private String message;
    private boolean readState;
    private String createdAt;

    public static NotificationResponseDto Alert(Notification notification) {
        return NotificationResponseDto.builder()
                .message(notification.getMessage())
                .readState(notification.isReadState())
                .createdAt(notification.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .type(notification.getType())
                .build();
    }
}
