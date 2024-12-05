package zerobase.tableNow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class QueueConfig {
    @Bean
    public Queue<Integer> waitingNumberQueue() {
        return new ConcurrentLinkedQueue<>();
    }
}
