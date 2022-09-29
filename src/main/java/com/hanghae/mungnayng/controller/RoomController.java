package com.hanghae.mungnayng.controller;

import com.hanghae.mungnayng.domain.Room.Dto.RoomInfoRequestDto;
import com.hanghae.mungnayng.domain.Room.Dto.RoomInfoResponseDto;
import com.hanghae.mungnayng.domain.Room.Dto.RoomInviteDto;
import com.hanghae.mungnayng.domain.Room.RoomInfo;
import com.hanghae.mungnayng.domain.UserDetailsImpl;
import com.hanghae.mungnayng.domain.member.Member;
import com.hanghae.mungnayng.repository.RoomInfoRepository;
import com.hanghae.mungnayng.service.RoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = {"채팅방 API 정보를 제공하는 Controller"})
@RequiredArgsConstructor
@RestController
public class RoomController {

    private final RoomService roomService;
    private final RoomInfoRepository roomInfoRepository;

    @ApiOperation(value = "채팅방 생성 메소드")
    @PostMapping(value = "/roomInfo")
    public ResponseEntity<?> createRoom(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                        @RequestBody RoomInfoRequestDto requestDto) {
        Member member = userDetails.getMember();
        return ResponseEntity.ok().body(Map.of("msg", "성공","roomInfo",roomService.createRoom(member,requestDto)));
    }

    @ApiOperation(value = "채팅방 정보 조회 메소드")
    @GetMapping(value = "/roomInfo")
    public ResponseEntity<?> getRoomInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        List<RoomInfoResponseDto> ResponseDtos = roomService.getRoomInfo(member);
        return ResponseEntity.ok().body(ResponseDtos);
    }

    @ApiOperation(value = "채팅방 나가기")
    @DeleteMapping(value = "/rooms/{itemId}")
    public ResponseEntity<?> deleteRoomInfo(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @PathVariable Long itemId) {
        Member member = userDetails.getMember();
        RoomInfo roomInfo = roomInfoRepository.findByMember_MemberIdAndItem_Id(member.getMemberId(), itemId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 채팅방 입니다."));
        roomService.deleteRoomInfo(member, roomInfo.getId());
        return ResponseEntity.ok().body(Map.of("success", true, "msg", "삭제 성공"));

    }

    @ApiOperation(value = "채팅방 입장 메소드")
    @PostMapping(value = "/rooms/{roomInfoId}")
    public ResponseEntity<?> inviteRoom(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                        @PathVariable Long roomInfoId, @RequestBody RoomInviteDto inviteDto) {
        Member member = userDetails.getMember();
        roomService.inviteRoom(member, roomInfoId, inviteDto);
        return ResponseEntity.ok().body(Map.of("msg", "초대 성공"));
    }


}
