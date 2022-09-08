package com.hanghae.mungnayng.repository;

import com.hanghae.mungnayng.domain.chat.Chat;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat>findByRoomDetail_RoomInfo_IdOrderByCreatedAtAsc(Long roomInfoId);


}
