package com.hanghae.mungnayng.domain.Notification.Dto;

import com.hanghae.mungnayng.domain.Notification.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDto {
    private Type type;
    private String message;
}
