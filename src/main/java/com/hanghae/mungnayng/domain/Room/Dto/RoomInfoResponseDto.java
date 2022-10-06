package com.hanghae.mungnayng.domain.Room.Dto;

import com.hanghae.mungnayng.domain.Room.RoomDetail;
import lombok.*;

import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RoomInfoResponseDto  {
    private Long roomInfoId;
    private String nickname;
    private String title;
    private String createdAt;


    public static RoomInfoResponseDto Info(RoomDetail roomDetail) {

        return RoomInfoResponseDto.builder()
                .roomInfoId(roomDetail.getRoomInfo().getId())
                .nickname(roomDetail.getRoomInfo().getNickname())
                .title(roomDetail.getRoomInfo().getTitle())
                .createdAt(roomDetail.getRoomInfo().getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();
    }
}
