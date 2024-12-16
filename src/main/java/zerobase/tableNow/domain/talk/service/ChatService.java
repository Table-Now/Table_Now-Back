package zerobase.tableNow.domain.talk.service;


import zerobase.tableNow.domain.talk.dto.ChatMessageDto;
import zerobase.tableNow.domain.talk.entity.ChatMessage;

import java.util.List;

public interface ChatService {
    ChatMessage saveMessage(ChatMessageDto messageDto);
    void handleUserJoin(String user);
    void handleUserLeave(String user);

    List<ChatMessageDto> getAllMessages();

}