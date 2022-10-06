package com.hanghae.mungnayng.domain.chat.dto;


import com.hanghae.mungnayng.domain.chat.Chat;
import com.hanghae.mungnayng.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.TimeZone;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto {
    private String content;
    private Long memberId;
    private String createdAt;

    private  Long roomInfoId;

    private Long chaId;
//    private String proPic;


    public ChatDto (Chat chat) {
        TimeZone tzSeoul = TimeZone.getTimeZone("Asia/Seoul");
        this.chaId=chat.getId();
        Member member = chat.getRoomDetail().getMember();
        this.memberId = member.getMemberId();
//        this.proPic = member.getProPic();
        this.content = chat.getMessage();
        this.roomInfoId = chat.getRoomInfoId();
        this.createdAt = chat.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(tzSeoul.toZoneId()));
    }
}