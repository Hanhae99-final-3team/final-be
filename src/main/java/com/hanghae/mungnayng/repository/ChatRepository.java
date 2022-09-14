package com.hanghae.mungnayng.repository;

import com.hanghae.mungnayng.domain.chat.Chat;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat>findByRoomDetail_RoomInfo_IdOrderByCreatedAtDesc(Long roomInfoId);

    Optional<Chat> findFirstByRoomDetail_RoomInfo_IdOrderByCreatedAtDesc(Long roomInfoId);


}
