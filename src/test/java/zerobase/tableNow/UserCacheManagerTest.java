//package zerobase.tableNow;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.redis.core.RedisTemplate;
//import zerobase.tableNow.config.redis.CustomCacheManager;
//
//
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//public class UserCacheManagerTest {
//
//    @Autowired
//    private CustomCacheManager customCacheManager;
//    @MockBean
//    private RedisTemplate<String, Integer> redisTemplate;
//
//    @BeforeEach
//    public void setUp() {
//        // 테스트 전에 목 객체를 설정할 수 있음
//        reset(redisTemplate);  // 각 테스트마다 초기화
//    }
//
//
//    @Test
//    public void testUpdateCache(){
//        String key = "waitingNumber123";
//        Integer value = 5;
//
//        //캐쉬 업데이트 테스트
//        customCacheManager.updateCache(key, value);
//
//        // RedisTemplate의 opsForValue().set 메서드가 호출되었는지 확인
//        verify(redisTemplate).opsForValue().set(key, value);
//    }
//    @Test
//    public void testGetCache() {
//        String key = "waitingNumber123";
//        Integer expectedValue = 5;
//
//        // RedisTemplate의 opsForValue().get 메서드가 호출되었을 때의 동작 설정
//       Mockito.when(redisTemplate.opsForValue().get(key)).thenReturn(expectedValue);
//
//        // 캐시 조회 테스트
//        Integer actualValue = customCacheManager.getCache(key);
//
//        // 결과 확인
//        assert(actualValue).equals(expectedValue);
//
//        // RedisTemplate의 opsForValue().get 메서드가 호출되었는지 확인
//        verify(redisTemplate).opsForValue().get(key);
//    }
//
//}
