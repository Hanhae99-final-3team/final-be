package com.hanghae.mungnayng.Redis;


import com.hanghae.mungnayng.domain.chat.dto.ChatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPub {
    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(ChannelTopic topic, ChatDto message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
