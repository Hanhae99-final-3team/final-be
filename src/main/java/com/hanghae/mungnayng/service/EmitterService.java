package com.hanghae.mungnayng.service;

import com.hanghae.mungnayng.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class EmitterService {

    private final EmitterRepository emitterRepository;
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60 * 24;

    public SseEmitter createEmitter(Long memberId) {
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        sseEmitter.onCompletion(
                () -> emitterRepository.remove(memberId)
        );
        sseEmitter.onTimeout(
                ()->emitterRepository.remove(memberId)
        );
        sseEmitter.onError(e -> {
            emitterRepository.remove(memberId);
        });
        emitterRepository.addOrReplaceEmitter(memberId, sseEmitter);
            return sseEmitter;
    }
}
