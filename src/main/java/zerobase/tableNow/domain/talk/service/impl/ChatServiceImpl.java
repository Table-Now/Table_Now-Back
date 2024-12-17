package zerobase.tableNow.domain.talk.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.tableNow.domain.talk.dto.ChatMessageDto;
import zerobase.tableNow.domain.talk.entity.ChatMessage;
import zerobase.tableNow.domain.talk.repository.ChatRepository;
import zerobase.tableNow.domain.talk.service.ChatService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CHAT_MESSAGES_KEY = "chat:messages"; // Redis에 저장할 키


    @Override
    @Transactional
    public ChatMessage saveMessage(ChatMessageDto messageDto) {
        //메세지 DB에 저장
        ChatMessage chatMessage = chatRepository.save(messageDto.toEntity());

        // 메시지를 Redis에 추가
        redisTemplate.opsForList().rightPush(CHAT_MESSAGES_KEY, messageDto);

        return chatMessage;
    }

    @Override
    public void handleUserJoin(String user) {
        ChatMessageDto joinMessage = ChatMessageDto.builder()
                .user(user)
                .content(user + "님이 채팅방에 입장했습니다.")
                .build();
        saveMessage(joinMessage);
    }

    @Override
    public void handleUserLeave(String user) {
        ChatMessageDto leaveMessage = ChatMessageDto.builder()
                .user(user)
                .content(user + "님이 채팅방에서 퇴장했습니다.")
                .build();
        saveMessage(leaveMessage);
    }

    @Override
    public List<ChatMessageDto> getAllMessages() {
        List<ChatMessage> messages = chatRepository.findAll(Sort.by("id"));
        return messages.stream()
                .map(ChatMessageDto::fromEntity)
                .collect(Collectors.toList());
    }


}
