package com.example.websocket.handler;

import com.example.websocket.dto.ChatMessage;
import com.example.websocket.dto.ChatRoom;
import com.example.websocket.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * 한 서버에 여러 클라이언트가 접속한다.
 * 서버는 여러 클라이언트가 발송한 메세지를 받아 처리해줄 Handler가 필요
 * 클라이언트로부터 받은 메세지를 console log에 출력, client로 환영 메세지를 보내는 역할
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final ChatService chatService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {}", payload);
//        TextMessage textMessage = new TextMessage("Welcome Chatting Server");
//        session.sendMessage(textMessage);
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        ChatRoom room = chatService.findRoomById(chatMessage.getRoomId());
        room.handleActions(session, chatMessage, chatService);
    }
}
