package com.hanghae.mungnayng.repository;

import com.hanghae.mungnayng.domain.Room.RoomDetail;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface RoomDetailRepository extends JpaRepository<RoomDetail, Long> {
    int countById(Long roomInfoId);
    Optional<RoomDetail> findByRoomInfo_IdAndMember_MemberId(Long infoId, Long memberId);
}
