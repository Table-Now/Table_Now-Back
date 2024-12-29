package zerobase.tableNow.domain.store.controller.manager.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import zerobase.tableNow.domain.store.controller.manager.dto.SettlementRequestDto;
import zerobase.tableNow.domain.store.controller.manager.dto.SettlementDto;
import zerobase.tableNow.domain.store.controller.manager.entity.SettlementEntity;
import zerobase.tableNow.domain.store.controller.manager.repository.SettlementRepository;
import zerobase.tableNow.domain.store.controller.manager.service.SettlementService;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.store.repository.StoreRepository;
import zerobase.tableNow.domain.user.entity.UsersEntity;
import zerobase.tableNow.domain.user.repository.UserRepository;
import zerobase.tableNow.exception.TableException;
import zerobase.tableNow.exception.type.ErrorCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SettlementServiceImpl implements SettlementService {
    private final SettlementRepository settlementRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;


    //실 결제 저장
    @Override
    @Transactional
    public void processSettlement(SettlementRequestDto settlementRequestDto) {
        Map<String, StoreEntity> storeCache = new HashMap<>();

        for (SettlementRequestDto.settlementDetail detail : settlementRequestDto.getSettlementDetails()) {
            // store 필드로 StoreEntity 조회
            StoreEntity store = storeCache.computeIfAbsent(detail.getStoreName(),
                    storeName -> storeRepository.findByStore(storeName)
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "매장을 찾을 수 없습니다: " + storeName)));

            SettlementEntity settlement = SettlementEntity.builder()
                    .store(store)
                    .storeName(store.getStore())  // StoreEntity의 store 필드값 사용
                    .menu(detail.getMenu())
                    .menuCount(detail.getMenuCount())
                    .totalPrice(detail.getTotalPrice())
                    .takeoutName(settlementRequestDto.getTakeoutName())
                    .takeoutPhone(settlementRequestDto.getTakeoutPhone())
                    .totalAmount(settlementRequestDto.getTotalAmount())
                    .build();

            settlementRepository.save(settlement);
        }
    }

    // 하루 매장별 매출
//    @Override
//    public List<SettlementDto> todaySettlement(Long storeId, LocalDateTime localDateTimeOfFrom, LocalDateTime localDateTimeOfTo) {
//        // 해당 날짜의 매장 결제 내역을 가져옴
//        List<SettlementEntity> settlementEntities =
//                settlementRepository.findAllByStoreIdAndCreateAtBetween(storeId, localDateTimeOfFrom, localDateTimeOfTo);
//
//        // 메뉴별 수량 및 총 매출 계산을 위한 Map 생성
//        Map<String, Long> menuCountMap = new HashMap<>();
//        Map<String, BigDecimal> menuTotalPriceMap = new HashMap<>();
//        BigDecimal totalAmount = BigDecimal.ZERO;
//
//        // 결제 내역을 순회하며 메뉴별 수량 및 매출 합산
//        for (SettlementEntity settlementEntity : settlementEntities) {
//            String menuName = settlementEntity.getMenu(); // 메뉴 이름
//            Long menuCount = settlementEntity.getMenuCount(); // 수량
//            BigDecimal menuPrice = settlementEntity.getTotalPrice(); // 가격
//
//            // 메뉴별 수량 합산
//            menuCountMap.put(menuName, menuCountMap.getOrDefault(menuName, 0L) + menuCount);
//
//            // 메뉴별 매출 합산
//            menuTotalPriceMap.put(menuName, menuTotalPriceMap.getOrDefault(menuName, BigDecimal.ZERO).add(menuPrice.multiply(BigDecimal.valueOf(menuCount))));
//
//            // 하루 총 매출 계산
//            totalAmount = totalAmount.add(menuPrice.multiply(BigDecimal.valueOf(menuCount)));
//        }
//
//        // 결과 리스트 생성
//        List<SettlementDto> result = new ArrayList<>();
//        for (String menuName : menuCountMap.keySet()) {
//            Long menuCount = menuCountMap.get(menuName);
//            BigDecimal menuTotalPrice = menuTotalPriceMap.get(menuName);
//
//            // SettlementTodayDto에 값 매핑
//            SettlementDto dto = SettlementDto.builder()
//                    .store(storeRepository.findById(storeId).orElseThrow())  // 매장 정보
//                    .menu(menuName)  // 메뉴 이름
//                    .menuCount(menuCount)  // 수량
//                    .totalPrice(menuTotalPrice)  // 총 매출
//                    .todayAmount(totalAmount)  // 하루 총 매출
//                    .build();
//
//            result.add(dto);
//        }
//
//        return result;
//    }

    @Override
    public List<SettlementDto> todaySettlementByUser(String username, LocalDateTime from, LocalDateTime to) {
        // 문자열 username을 UsersEntity로 변환
        UsersEntity userEntity = userRepository.findByUser(username)
                .orElseThrow(() -> new TableException(ErrorCode.USER_NOT_FOUND));

        // 사용자의 모든 상점 조회
        List<StoreEntity> userStores = storeRepository.findAllByUser(userEntity);

        // 모든 상점 ID 추출
        List<Long> storeIds = userStores.stream()
                .map(StoreEntity::getId)
                .collect(Collectors.toList());

        // 상점 ID 리스트로 결제 내역 조회
        List<SettlementEntity> settlementEntities =
                settlementRepository.findAllByStoreIdInAndCreateAtBetween(storeIds, from, to);

        // 통계 계산 (기존 로직 재사용)
        Map<String, Long> menuCountMap = new HashMap<>();
        Map<String, BigDecimal> menuTotalPriceMap = new HashMap<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (SettlementEntity settlementEntity : settlementEntities) {
            String menuName = settlementEntity.getMenu();
            Long menuCount = settlementEntity.getMenuCount();
            BigDecimal menuPrice = settlementEntity.getTotalPrice();

            menuCountMap.put(menuName, menuCountMap.getOrDefault(menuName, 0L) + menuCount);
            menuTotalPriceMap.put(menuName, menuTotalPriceMap.getOrDefault(menuName, BigDecimal.ZERO)
                    .add(menuPrice.multiply(BigDecimal.valueOf(menuCount))));
            totalAmount = totalAmount.add(menuPrice.multiply(BigDecimal.valueOf(menuCount)));
        }

        List<SettlementDto> result = new ArrayList<>();
        for (String menuName : menuCountMap.keySet()) {
            Long menuCount = menuCountMap.get(menuName);
            BigDecimal menuTotalPrice = menuTotalPriceMap.get(menuName);

            SettlementDto dto = SettlementDto.builder()
                    .store(null) // 필요시 store 정보를 추가 처리
                    .menu(menuName)
                    .menuCount(menuCount)
                    .totalPrice(menuTotalPrice)
                    .todayAmount(totalAmount)
                    .build();

            result.add(dto);
        }

        return result;
    }



    // 특정 기간별 매출 현황
    @Override
    public List<SettlementDto> periodSettlement(Long storeId, LocalDate to, LocalDate from) {
        // LocalDate를 LocalDateTime으로 변환
        LocalDateTime localDateTimeFrom = from.atStartOfDay();  // 시작 날짜의 시작 시점 (00:00)
        LocalDateTime localDateTimeTo = to.atTime(23, 59, 59, 999999999);  // 끝 날짜의 끝 시점 (23:59:59.999999999)

        // 해당 날짜 범위의 매장 결제 내역을 가져옴
        List<SettlementEntity> settlementEntities =
                settlementRepository.findAllByStoreIdAndCreateAtBetween(storeId, localDateTimeFrom, localDateTimeTo);

        // 메뉴별 수량 및 총 매출 계산을 위한 Map 생성
        Map<String, Long> menuCountMap = new HashMap<>();
        Map<String, BigDecimal> menuTotalPriceMap = new HashMap<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        // 결제 내역을 순회하며 메뉴별 수량 및 매출 합산
        for (SettlementEntity settlementEntity : settlementEntities) {
            String menuName = settlementEntity.getMenu(); // 메뉴 이름
            Long menuCount = settlementEntity.getMenuCount(); // 수량
            BigDecimal menuPrice = settlementEntity.getTotalPrice(); // 가격

            // 메뉴별 수량 합산
            menuCountMap.put(menuName, menuCountMap.getOrDefault(menuName, 0L) + menuCount);

            // 메뉴별 매출 합산
            menuTotalPriceMap.put(menuName, menuTotalPriceMap.getOrDefault(menuName, BigDecimal.ZERO).add(menuPrice.multiply(BigDecimal.valueOf(menuCount))));

            // 하루 총 매출 계산
            totalAmount = totalAmount.add(menuPrice.multiply(BigDecimal.valueOf(menuCount)));
        }

        // 매장 정보 한 번만 조회
        StoreEntity store = storeRepository.findById(storeId).orElseThrow(() ->
                new TableException(ErrorCode.PRODUCT_NOT_FOUND));

        // 결과 리스트 생성
        List<SettlementDto> result = new ArrayList<>();
        for (String menuName : menuCountMap.keySet()) {
            Long menuCount = menuCountMap.get(menuName);
            BigDecimal menuTotalPrice = menuTotalPriceMap.get(menuName);

            // SettlementTodayDto에 값 매핑
            SettlementDto dto = SettlementDto.builder()
                    .store(store)  // 미리 조회한 매장 정보 재사용
                    .menu(menuName)  // 메뉴 이름
                    .menuCount(menuCount)  // 수량
                    .totalPrice(menuTotalPrice)  // 총 매출
                    .todayAmount(totalAmount)  // 하루 총 매출
                    .build();

            result.add(dto);
        }

        return result;
    }
}
