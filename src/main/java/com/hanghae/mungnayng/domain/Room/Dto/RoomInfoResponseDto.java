package com.hanghae.mungnayng.domain.Room.Dto;

import com.hanghae.mungnayng.domain.Room.RoomInfo;
import com.hanghae.mungnayng.domain.item.Item;
import lombok.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RoomInfoResponseDto  {
    private Long roomInfoId;
    private String nickname;
    private String createdAt;


    public static RoomInfoResponseDto Info(RoomInfo roomInfo) {
        Item item = new Item();
        return RoomInfoResponseDto.builder()
                .roomInfoId(roomInfo.getId())
                .nickname(item.getNickname())
                .createdAt(roomInfo.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();
    }
}
