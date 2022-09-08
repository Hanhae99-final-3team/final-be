package com.hanghae.mungnayng.config;

import com.hanghae.mungnayng.config.handler.ChatHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatHandler chatHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        ///ws/chat Endpoint로 Handshake가 이루어짐
            registry.addHandler(chatHandler, "/")
                .setAllowedOrigins("*")
                .withSockJS()
                .setClientLibraryUrl("http://localhost:8080/js/sockjs.min.js");
    }
    //setAllowedOrigins("*")에서 *라는 와일드 카드를 사용하면
    //보안상의 문제로 전체를 허용하는 것보다 직접 하나씩 지정해주어야 한다고 한다.

}
