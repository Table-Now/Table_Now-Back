package zerobase.tableNow.domain.talk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import zerobase.tableNow.domain.talk.dto.ChatMessageDto;
import zerobase.tableNow.domain.talk.repository.ChatRepository;
import zerobase.tableNow.domain.talk.service.ChatService;

import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final ChatRepository chatRepository;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessageDto sendMessage(@Payload ChatMessageDto chatMessage) {
        chatService.saveMessage(chatMessage);
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessageDto addUser(
            @Payload ChatMessageDto chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        // WebSocket 세션에 username 추가
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("user", chatMessage.getUser());
        chatService.handleUserJoin(chatMessage.getUser());
        return chatMessage;
    }

    @GetMapping("/chat/messages")
    public ResponseEntity<List<ChatMessageDto>> getAllMessages() {
        List<ChatMessageDto> messages = chatService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

}
