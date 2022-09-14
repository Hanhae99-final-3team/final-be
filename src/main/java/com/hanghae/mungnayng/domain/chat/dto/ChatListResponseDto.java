package com.hanghae.mungnayng.domain.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatListResponseDto {
    private Long roomInfoId;
    private String recentChat;
    private String createdAt;

    public ChatListResponseDto(Long roomId, ChatDto requestDto) {
        this.roomInfoId = roomId;
        this.recentChat = requestDto.getContent();
        this.createdAt = requestDto.getCreatedAt();
    }
}

