package com.hanghae.mungnayng.domain.Room.Dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoomInviteDto {
    private Long memberId;
    private String nickname;
}
