//package zerobase.tableNow.config.redis.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import zerobase.tableNow.config.redis.CustomCacheManager;
//import zerobase.tableNow.config.redis.Example;
//import zerobase.tableNow.config.redis.ExampleRepository;
//
//@Service
//@RequiredArgsConstructor
//public class ExampleService {
//    private final CustomCacheManager customCacheManager;
//    private final ExampleRepository exampleRepository;
//
//    // 데이터베이스와 Redis 캐시를 동시에 업데이트하는 로직
//    public void updateDatabase(String key, Integer value) {
//        // 데이터베이스 변경 로직
//        Example example = exampleRepository.findByKey(key);
//        if (example == null) {
//            example = new Example();
//            example.setKey(key);
//        }
//        example.setValue(value);
//        exampleRepository.save(example);
//
//        // Redis 캐시 업데이트
//        customCacheManager.updateCache(key, value);
//    }
//
//    @Transactional(readOnly = true)
//    public Integer getValue(String key) {
//        // 캐시에서 값 조회
//        Integer cachedValue = customCacheManager.getCache(key);
//        if (cachedValue != null) {
//            return cachedValue;
//        }
//
//        // 캐시에서 값이 없으면, 데이터베이스에서 조회하고 캐시 갱신
//        Example example = exampleRepository.findByKey(key);
//        if (example != null) {
//            customCacheManager.updateCache(key, example.getValue());
//            return example.getValue();
//        }
//
//        return null;
//    }
//}
