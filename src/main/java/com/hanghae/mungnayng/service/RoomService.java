package com.hanghae.mungnayng.service;

import com.hanghae.mungnayng.domain.Room.Dto.RoomInfoRequestDto;
import com.hanghae.mungnayng.domain.Room.Dto.RoomInfoResponseDto;
import com.hanghae.mungnayng.domain.Room.Dto.RoomInviteDto;
import com.hanghae.mungnayng.domain.Room.RoomDetail;
import com.hanghae.mungnayng.domain.Room.RoomInfo;
import com.hanghae.mungnayng.domain.chat.Chat;
import com.hanghae.mungnayng.domain.member.Member;
import com.hanghae.mungnayng.repository.ChatRepository;
import com.hanghae.mungnayng.repository.MemberRepository;
import com.hanghae.mungnayng.repository.RoomDetailRepository;
import com.hanghae.mungnayng.repository.RoomInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomInfoRepository roomInfoRepository;
    private final RoomDetailRepository roomDetailsRepository;
    private final MemberRepository memberRepository;

    private final ChatRepository chatRepository;


    public RoomInfoResponseDto createRoom(Long me, Long memberId, String nickname) {
        Member member = memberRepository.findById(me).orElseThrow();
        RoomInfoRequestDto RequestDto = new RoomInfoRequestDto(memberId, nickname);
        return createRoom(member, RequestDto);
    }

    public RoomInfoResponseDto createRoom(Member member, RoomInfoRequestDto requestDto) {
        memberRepository.findById(member.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));
        RoomInfo roomInfo = RoomInfo.builder()
                .member(member)
                .nickname(requestDto.getNickname())
                .roomDetail(new ArrayList<>())
                .build();
        RoomDetail roomDetail = RoomDetail.builder()
                .member(member)
                .roomInfo(roomInfo)
                .build();
        int Cnt = roomDetailsRepository.countById(roomInfo.getId());
        if( Cnt > 2 ) throw new IllegalArgumentException("채팅 내역이 이미 존재합니다.");

        roomInfo.getRoomDetail().add(roomDetail);
        roomInfoRepository.save(roomInfo);
        return RoomInfoResponseDto.Info(roomInfo);
    }


    @Transactional
    public void updateLastReadChat(Long roomId, Long memberId) {
        RoomDetail detail = roomDetailsRepository.findByRoomInfo_IdAndMember_MemberId(roomId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방에 속해있지 않은 회원입니다."));

        Chat chat = chatRepository.findFirstByRoomDetail_RoomInfo_IdOrderByCreatedAtDesc(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅 내역이 존재하지 않습니다."));

        detail.updateChatId(chat.getId());
    }

    @Transactional(readOnly = true)
    public List<RoomInfoResponseDto> getRoomInfo(String memberId) {
        Member member = memberRepository.findById(Long.parseLong(memberId)).orElseThrow();
        return getRoomInfo(member);
    }

    @Transactional(readOnly = true)
    public List<RoomInfoResponseDto> getRoomInfo(Member member) {
        List<RoomInfo> allByOrderByModifiedAtDesc = roomInfoRepository.findAllByMemberOrderByModifiedAtDesc(member);
        return allByOrderByModifiedAtDesc.stream()
                .map(RoomInfoResponseDto::Info)
                .collect(Collectors.toList());

        /*차후 채팅창 연결시 필요 할 수도 있으니 주석처리 함.*/
//                List<RoomInfoResponseDto> dtos = new ArrayList<>();
//
//                for (RoomInfo roomInfo : allByOrderByModifiedAtDesc) {
//                        Long roomInfoId = roomInfo.getId();
//                        String nickname = member.getNickname();
//                        String createdAt = String.valueOf(roomInfo.getCreatedAt());
//
//                        RoomInfoResponseDto responseDto = new RoomInfoResponseDto(roomInfoId, nickname,createdAt);
//                        dtos.add(responseDto);
//                }
    }

    @Transactional
    public void deleteRoomInfo(Member member, Long roomInfoId) {
        RoomInfo roomInfo = roomInfoRepository.findById(roomInfoId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅창입니다."));
        if (!member.getMemberId().equals(roomInfo.getMember().getMemberId()))
            throw new IllegalArgumentException("채팅방에 존재하지 않는 유저입니다.");
        roomInfoRepository.delete(roomInfo);
    }

    @Transactional
    public void inviteRoom(Long memberId, Long roomId, RoomInviteDto inviteDto) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        inviteRoom(member, roomId, inviteDto);
    }

    public void inviteRoom(Member me, Long roomInfoId, RoomInviteDto inviteDto) {
        RoomInfo roomInfo = roomInfoRepository.findById(roomInfoId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅창 입니다."));
        if (!me.getMemberId().equals(roomInfo.getMember().getMemberId()))
            throw new IllegalArgumentException("권한이 없습니다.");
        Member member = memberRepository.findById(inviteDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("초대 대상이 올바르지 않습니다."));
        RoomDetail roomDetail = roomDetailsRepository.findByRoomInfo_IdAndMember_MemberId(roomInfoId, inviteDto.getMemberId())
                .orElse(new RoomDetail(roomInfo, member));
        roomDetailsRepository.save(roomDetail);

    }
}
