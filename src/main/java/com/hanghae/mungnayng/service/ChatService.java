package com.hanghae.mungnayng.service;

import com.hanghae.mungnayng.domain.Room.RoomDetail;
import com.hanghae.mungnayng.domain.chat.Chat;
import com.hanghae.mungnayng.domain.chat.dto.ChatDto;
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
    private final RoomDetailRepository roomDetailRepository;
    private final ChatRepository chatRepository;

    @Transactional
    public ChatDto saveChat(Long roomId, ChatDto message) {
        RoomDetail roomDetail = roomDetailRepository.findByRoomInfo_IdAndMember_MemberId(roomId, message.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("채팅방에 관한 정보가 없습니다."));

        Chat chat = Chat.builder()
                .roomDetail(roomDetail)
                .message(message.getContent())
                .build();

        ChatDto chatDto = new ChatDto(chatRepository.save(chat));
        return chatDto;
    }


    /*읽지 않은 최근 채팅 저장하기*/
//    public List<ChatDto> getChat(Long roomId) {
//        List<Chat> chats = chatRepository.findByRoomDetail_RoomInfo_InOrderByCreatedAtAsc(roomId);
//
//        return chats.stream()
//                .map(ChatDto::new)
//                .collect(Collectors.toList());
//
//    }

}
