package com.hanghae.mungnayng.domain.chat.dto;


import com.hanghae.mungnayng.domain.chat.Chat;
import com.hanghae.mungnayng.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto {
    private String content;
    private Long memberId;
    private String CreatedAt;


    public ChatDto (Chat chat) {
        Member member = chat.getRoomDetail().getMember();
        this.memberId = member.getMemberId();
        this.content = chat.getMessage();
        this.CreatedAt = chat.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
