package zerobase.tableNow.domain.talk.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.tableNow.domain.talk.entity.ChatMessage;

@Repository
public interface ChatRepository extends JpaRepository<ChatMessage, Long> {

}