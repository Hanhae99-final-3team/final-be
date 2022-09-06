package com.hanghae.mungnayng.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Log4j2
public class ChatController {
/*채팅방 입장*/
//    @GetMapping("/rooms/{roomId}")/*roomId는 클라이언트에서 받아옴*/
//    public ResponseEntity<?> getRoomChat(
//    @PathVariable Long roomId
//    ) {
//
//       List<chatDto> chat =chatService.getRoomChat(roomId);
//
//       return ResponseEntity.ok().body(chat);
//    }
}