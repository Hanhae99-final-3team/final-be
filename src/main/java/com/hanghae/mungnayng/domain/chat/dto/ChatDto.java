package com.hanghae.mungnayng.domain.chat.dto;


import com.hanghae.mungnayng.domain.chat.Chat;
import com.hanghae.mungnayng.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static java.util.Map.entry;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto {
    private String content;
    private Long memberId;
    private String createdAt;

//    private String proPic;


    public ChatDto (Chat chat) {
        Member member = chat.getRoomDetail().getMember();
        this.memberId = member.getMemberId();
//        this.proPic = member.getProPic();
        this.content = chat.getMessage();
        this.createdAt = chat.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.of("Asia/Seoul")));
    }
}