package com.hanghae.mungnayng.repository;


import com.hanghae.mungnayng.domain.Room.RoomInfo;
import com.hanghae.mungnayng.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomInfoRepository extends JpaRepository<RoomInfo, Long> {
    List<RoomInfo> findAllByMemberOrderByModifiedAtDesc(Member member);
    Optional<RoomInfo> findRoomInfoByItem_Id(Long itemId);
//    Optional<RoomInfo>Item_RoomInfo_Id(Long itemId);
    Optional<RoomInfo> findByMember_MemberIdAndItem_Id(Long memberId, Long itemId);
}