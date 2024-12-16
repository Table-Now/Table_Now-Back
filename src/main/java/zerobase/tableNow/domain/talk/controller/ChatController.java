package zerobase.tableNow.domain.talk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import zerobase.tableNow.domain.talk.dto.ChatMessageDto;
import zerobase.tableNow.domain.talk.service.ChatService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

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
        headerAccessor.getSessionAttributes().put("user", chatMessage.getUser());
        chatService.handleUserJoin(chatMessage.getUser());
        return chatMessage;
    }

    // Redis에서 모든 메시지를 가져오는 API
    @GetMapping("/chat/messages")
    @ResponseBody
    public List<ChatMessageDto> getAllMessages() {
        return chatService.getAllMessages();
    }
}
