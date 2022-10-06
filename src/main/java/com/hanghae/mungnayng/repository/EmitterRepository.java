package com.hanghae.mungnayng.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EmitterRepository {
    private Map<Long, SseEmitter> userSseEmitterMap = new ConcurrentHashMap<>();

    public void addOrReplaceEmitter(Long memberId, SseEmitter sseEmitter) {
        userSseEmitterMap.put(memberId, sseEmitter);
    }

    public void remove(Long memberId) {
        if(userSseEmitterMap !=null) {
            userSseEmitterMap.remove(memberId);
        }
    }

    public Optional<SseEmitter> get(Long memberId){
        return Optional.ofNullable(userSseEmitterMap.get(memberId));
    }
}
