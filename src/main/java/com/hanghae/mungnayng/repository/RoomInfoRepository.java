package com.hanghae.mungnayng.repository;


import com.hanghae.mungnayng.domain.Room.RoomInfo;
import com.hanghae.mungnayng.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomInfoRepository extends JpaRepository<RoomInfo, Long> {
    List<RoomInfo> findAllByMemberOrderByModifiedAtDesc(Member member);
}
