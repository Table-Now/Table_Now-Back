package zerobase.tableNow.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import zerobase.tableNow.domain.store.dto.StoreDto;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class CustomCacheManager {
    private final RedisTemplate<String,Integer> redisTemplate;
//    private final RedisTemplate<String, Object> storeRedisTemplate;



    // 대기번호 업데이트
    public void updateCache(String key, Integer value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // 대기번호 조회
    public Integer getCache(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // 상점 목록 캐싱
//    public void cacheStoreList(String key, List<StoreDto> storeList) {
//        storeRedisTemplate.opsForValue().set(key, storeList, 60, TimeUnit.MINUTES);
//    }

    //상점 목록 조회
//    public List<StoreDto> getStoreList(String key) {
//        Object cachedValue = storeRedisTemplate.opsForValue().get(key);
//
//        if (cachedValue instanceof List<?>) {
//            try {
//                // 캐시된 값을 List<StoreDto>로 변환하여 반환
//                return (List<StoreDto>) cachedValue;
//            } catch (ClassCastException e) {
//                // 타입 변환 실패 시 null 반환
//                return null;
//            }
//        }
//        return null;
//    }
}
