package com.hanghae.mungnayng.domain.Room.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoomInfoRequestDto {
    private Long memberId;
    private String nickname;
}
