package com.hanghae.mungnayng.controller;

import com.hanghae.mungnayng.domain.Room.Dto.RoomInfoResponseDto;
import com.hanghae.mungnayng.domain.Room.RoomInfo;
import com.hanghae.mungnayng.domain.UserDetailsImpl;
import com.hanghae.mungnayng.domain.member.Member;
import com.hanghae.mungnayng.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class RoomController {

    private final RoomService roomService;

    @PostMapping(value = "/roomInfo")
    public ResponseEntity<?> createRoom(@AuthenticationPrincipal UserDetailsImpl userDetails) {
     Member member = userDetails.getMember();
    roomService.createRoom(member.getMemberId());
        return ResponseEntity.ok().body(Map.of("msg","생성 완료"));
    }

    @GetMapping(value = "/roomInfo")
    public ResponseEntity<?> getRoomInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        List<RoomInfoResponseDto> ResponseDtos= roomService.getRoomInfo(member);
        return ResponseEntity.ok().body(ResponseDtos);
    }

    @DeleteMapping(value = "/rooms/{roomInfoId}/member/{memberId}")
        public ResponseEntity<?> deleteRoomInfo(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                @PathVariable Long roomInfoId) {
        Member member = userDetails.getMember();
        roomService.deleteRoomInfo(member, roomInfoId);
        return ResponseEntity.ok().body(Map.of("success", true, "msg", "삭제 성공"));

    }

}
