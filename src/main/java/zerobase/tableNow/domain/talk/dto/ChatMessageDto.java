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
    private ChatMessage.MessageType type;

    public ChatMessage toEntity() {
        return ChatMessage.builder()
                .user(this.user)
                .content(this.content)
                .type(this.type)
                .build();
    }
}
