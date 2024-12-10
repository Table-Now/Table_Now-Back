//package zerobase.tableNow.config.redis.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class QueueStatusService {
//    private final RedisTemplate<String, Integer> redisTemplate;
//
//    private static final String WAITING_NUMBER_QUEUE = "waitingNumberQueue";
//
//    /**
//     * 대기번호 큐의 상태를 조회
//     */
//    public List<Integer> getQueueStatus() {
//        return redisTemplate.opsForList().range(WAITING_NUMBER_QUEUE, 0, -1);
//    }
//}
