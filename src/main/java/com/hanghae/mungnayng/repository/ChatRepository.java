package com.hanghae.mungnayng.repository;

import com.hanghae.mungnayng.domain.chat.Chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat>findByRoomDetail_Member_MemberIdOrderByCreatedAtDesc(Long memberId);
    List<Chat>findByRoomDetail_RoomInfo_IdOrderByCreatedAtDesc(Long roomInfoId);
    Optional<Chat> findFirstByRoomDetail_RoomInfo_IdOrderByCreatedAtDesc(Long roomInfoId);

//    @Query(nativeQuery = true, value =
//            "select item_id, member_id FROM chat c" +
//            "left join room_detail rd on rd.id = c.room_detail_id" +
//            "order by desc")
//    List<Chat> findChatData(Long itemId, Long memberId);


}
