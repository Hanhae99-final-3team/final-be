package com.hanghae.mungnayng.controller;


import com.hanghae.mungnayng.domain.chat.dto.ChatDto;
import com.hanghae.mungnayng.service.ChatService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Log4j2
@RestController
@AllArgsConstructor
public class ChatController {

    private final ChatService chatService;
    /*채팅방과 연결*/
    @GetMapping("/room/{roomId}")
    public ResponseEntity<?> getRoomChat(@PathVariable Long roomId) {

    List<ChatDto> chats = chatService.getChat(roomId);

        return ResponseEntity.ok().body(chats);
    }
}

//    @GetMapping("/chat")
//    public String chatGET(){
//
//        log.info("@ChatController, chat GET()");
//
//        return "chat";
//    }