package com.hanghae.mungnayng.config.handler;

import com.hanghae.mungnayng.service.RoomService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.messaging.simp.stomp.StompCommand.CONNECT;
import static org.springframework.messaging.simp.stomp.StompCommand.UNSUBSCRIBE;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompInterceptor implements ChannelInterceptor {

//    private final TokenProvider tokenProvider;

    private final RoomService roomService;

//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//
//        StompCommand command = accessor.getCommand();
//        if (command == CONNECT) {
//            List<String> accessToken = accessor.getNativeHeader("Authorization");
//
//            // 엑세스토큰이 없거나 토큰 검증에 실패한 경우
//            if (accessToken == null || !tokenProvider.validateToken(accessToken.get(0).substring(7))) {
//                log.error("connection fail");
//                throw new IllegalArgumentException("Invalid Token");
//            }
//
//            log.info("connection success");
//        }
//
//        return message;
//    }

    @Override
    public void postSend(Message message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        StompCommand command = accessor.getCommand();
        // unsubscribe 상태 감지
        if (command == UNSUBSCRIBE) {
            List<String> disconnectedMemberId = accessor.getNativeHeader("memberId");
            List<String> disconnectedRoomId = accessor.getNativeHeader("roomId");
            // 채팅방 unsubscribe 상태 감지
            if (disconnectedMemberId != null && disconnectedRoomId != null) {
                Long memberId = Long.parseLong(disconnectedMemberId.get(0));
                Long roomId = Long.parseLong(disconnectedRoomId.get(0));

                roomService.updateLastReadChat(roomId, memberId);
                log.info("member #" + memberId + " leave chat room #" + roomId);
            }
        }
    }
}
