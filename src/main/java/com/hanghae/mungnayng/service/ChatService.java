package com.hanghae.mungnayng.service;

import com.hanghae.mungnayng.domain.Room.RoomDetail;
import com.hanghae.mungnayng.domain.Room.RoomInfo;
import com.hanghae.mungnayng.domain.chat.Chat;
import com.hanghae.mungnayng.domain.chat.dto.ChatDto;
import com.hanghae.mungnayng.domain.member.Member;
import com.hanghae.mungnayng.repository.ChatRepository;
import com.hanghae.mungnayng.repository.RoomDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    private final RoomDetailRepository roomDetailsRepository;
    private final ChatRepository chatRepository;

    @Transactional
    public ChatDto saveChat(Long roomId, ChatDto message) {
        log.info(roomId.toString());
        log.info(message.toString());
        RoomDetail roomDetail = roomDetailsRepository.findByRoomInfo_IdAndMember_MemberId(roomId, message.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("채팅방에 관한 정보가 없습니다."));
        roomDetail.getRoomInfo().updateRecentChat(message.getContent());
        Chat chat = Chat.builder()
                .roomDetail(roomDetail)
                .message(message.getContent())
                .build();

        ChatDto chatDto = new ChatDto(chatRepository.save(chat));
        return chatDto;
    }


    /*채팅 보내기*/
    @Transactional
    public List<ChatDto> getChat(RoomInfo roomInfo) {
        List<Chat> chats = chatRepository.findByRoomDetail_RoomInfo_IdOrderByCreatedAtDesc(roomInfo.getId());
        return chats.stream()
                .map(ChatDto::new)
                .collect(Collectors.toList());
    }
}
