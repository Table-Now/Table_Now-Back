package zerobase.tableNow.domain.talk.entity;

import jakarta.persistence.*;
import lombok.*;
import zerobase.tableNow.domain.baseEntity.BaseEntity;
import zerobase.tableNow.domain.user.entity.UsersEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "chat_messages")
public class ChatMessage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; //내용

//    @Enumerated(EnumType.STRING)
//    private MessageType type;
//
//    public enum MessageType {
//        CHAT, JOIN, LEAVE
//    }
}