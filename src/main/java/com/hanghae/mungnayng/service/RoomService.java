package com.hanghae.mungnayng.service;


import com.hanghae.mungnayng.domain.Room.Dto.RoomInfoResponseDto;
import com.hanghae.mungnayng.domain.Room.Dto.RoomInviteDto;
import com.hanghae.mungnayng.domain.Room.RoomDetail;
import com.hanghae.mungnayng.domain.Room.RoomInfo;
import com.hanghae.mungnayng.domain.member.Member;
import com.hanghae.mungnayng.repository.MemberRepository;
import com.hanghae.mungnayng.repository.RoomDetailRepository;
import com.hanghae.mungnayng.repository.RoomInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RoomService {
        private final RoomInfoRepository roomInfoRepository;
        private final RoomDetailRepository roomDetailRepository;
        private final MemberRepository memberRepository;

        public RoomInfoResponseDto createRoom(Long memberId) {
                Member member = memberRepository.findById(memberId)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));
                RoomInfo roomInfo = RoomInfo.builder()
                        .member(member)
                        .nickname(member.getNickname())
                        .build();
                roomInfoRepository.save(roomInfo);
                return RoomInfoResponseDto.Info(roomInfo);
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
        public void deleteRoomInfo(Member member, Long roomInfoId ) {
                RoomInfo roomInfo = roomInfoRepository.findById(roomInfoId)
                        .orElseThrow(()-> new IllegalArgumentException("존재하지 채팅창입니다."));
                if(!member.getMemberId().equals(roomInfo.getMember().getMemberId()))
                throw new IllegalArgumentException("채팅방에 존재하지 않는 유저입니다.");
                roomInfoRepository.delete(roomInfo);
        }

        public void inviteRoom(Member me, Long roomInfoId, RoomInviteDto inviteDto) {
                RoomInfo roomInfo = roomInfoRepository.findById(roomInfoId)
                        .orElseThrow(()->new IllegalArgumentException("존재하지 않는 채팅창 입니다."));
                if(!me.getNickname().equals(roomInfo.getNickname()))
                throw new IllegalArgumentException("자신의 채팅방이 아닙니다.");

                for(Long id : inviteDto.getMemberId() ) {
                        Member member = memberRepository.findById(id)
                                .orElseThrow(()-> new IllegalArgumentException("초대 대상이 올바르지 않습니다."));
                        RoomDetail roomDetail = roomDetailRepository.findByRoomInfo_IdAndMember_MemberId(roomInfoId, id)
                                .orElse(new RoomDetail(roomInfo, member));
                        roomDetailRepository.save(roomDetail);
                }
        }
}
