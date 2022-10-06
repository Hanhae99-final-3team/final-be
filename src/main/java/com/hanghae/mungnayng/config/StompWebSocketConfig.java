package com.hanghae.mungnayng.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker/*stomp적용*/
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

//    private final ChatHandler chatHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/wss")
                .setAllowedOriginPatterns("*")
                .withSockJS();

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/pub");
        registry.enableSimpleBroker("/sub");
    }

}



//@Override
//public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(chatHandler, "/ws/chat")
//        .setAllowedOriginPatterns("http://*:8080", "http://*.*.*.*:8080")
//        .withSockJS()
//        .setClientLibraryUrl("http://localhost:8080/myapp/js/sock-client.js");
//withSockJS를 추가하면 sockJS를 사용 가능하다.
//.setAllowedOrigins("*")는 와일드 카드로 전체를 허용해 주니 직접 지정하는 것이 보안상 더 좋음.
//위의 코드는 지정된 Origins 목록을 활성화 한 방법을 사용.
//}