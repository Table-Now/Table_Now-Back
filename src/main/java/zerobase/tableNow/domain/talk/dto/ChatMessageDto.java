package zerobase.tableNow.domain.talk.dto;

import lombok.*;
import zerobase.tableNow.domain.talk.entity.ChatMessage;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDto {
    private String user;
    private String content;

    public ChatMessage toEntity() {
        return ChatMessage.builder()
                .user(this.user)
                .content(this.content)
                .build();
    }

    public static ChatMessageDto fromEntity(ChatMessage chatMessage) {
        return ChatMessageDto.builder()
                .user(chatMessage.getUser())
                .content(chatMessage.getContent())
                .build();
    }
}
