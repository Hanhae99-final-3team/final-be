package com.hanghae.mungnayng.Redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae.mungnayng.domain.chat.dto.ChatDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSub implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessageSendingOperations messageSendingOperations;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = redisTemplate.getStringSerializer().deserialize(message.getBody());
            ChatDto chatMessage = objectMapper.readValue(publishMessage, ChatDto.class);
            log.info(chatMessage.getRoomInfoId().toString());
            messageSendingOperations.convertAndSend("/sub/chat/room/" + chatMessage.getRoomInfoId(), chatMessage); /*메세지 보내기*/
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
