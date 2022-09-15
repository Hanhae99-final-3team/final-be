package com.hanghae.mungnayng.domain.Room.Dto;

import com.hanghae.mungnayng.domain.Room.RoomInfo;
import lombok.*;

import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RoomInfoResponseDto  {
    private Long roomInfoId;
    private String nickname;
    private String createdAt;


    public static RoomInfoResponseDto Info(RoomInfo roomInfo) {
        RoomInfoRequestDto requestDto = new RoomInfoRequestDto();
        return RoomInfoResponseDto.builder()
                .roomInfoId(roomInfo.getId())
                .nickname(requestDto.getNickname())
                .createdAt(roomInfo.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();
    }

}
