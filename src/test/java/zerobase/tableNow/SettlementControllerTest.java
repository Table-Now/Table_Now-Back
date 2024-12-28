package zerobase.tableNow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import zerobase.tableNow.domain.store.controller.manager.dto.SettlementRequestDto;
import zerobase.tableNow.domain.store.controller.manager.dto.SettlementDto;
import zerobase.tableNow.domain.store.controller.manager.entity.SettlementEntity;
import zerobase.tableNow.domain.store.controller.manager.repository.SettlementRepository;
import zerobase.tableNow.domain.store.controller.manager.service.impl.SettlementServiceImpl;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.store.repository.StoreRepository;
import zerobase.tableNow.domain.user.entity.UsersEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SettlementControllerTest {
    @Mock
    private SettlementRepository settlementRepository;
    @Mock
    private StoreRepository storeRepository;
    @InjectMocks
    private SettlementServiceImpl settlementService;

    @Test
    @DisplayName("결제 정보 등록 - 사용자가 로그인되어 있을 때 결제 정보가 정상적으로 저장된다.")
    void processSettlement() {
        // Given: Mock 데이터 설정
        String mockUserId = "testUser"; // Mock 사용자 ID
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(mockUserId, null)
        );

        // 사용자 Mock 데이터 생성
        UsersEntity mockUser = new UsersEntity();
        mockUser.setUser(mockUserId);
        mockUser.setPhone("123-456-7890");

        // StoreEntity 생성
        StoreEntity mockStore = new StoreEntity();
        mockStore.setId(1L);
        mockStore.setStore("Test Store");

        // 결제 요청 DTO 생성
        SettlementRequestDto settlementRequestDto = new SettlementRequestDto();
        settlementRequestDto.setTakeoutName("John Doe");
        settlementRequestDto.setTakeoutPhone("123-456-7890");
        settlementRequestDto.setTotalAmount(BigDecimal.valueOf(10000));

        // SettlementDetail 생성
        SettlementRequestDto.settlementDetail detail = new SettlementRequestDto.settlementDetail();
        detail.setStore(mockStore); // StoreEntity 객체 전달
        detail.setMenu("Test Menu");
        detail.setMenuCount(2L);
        detail.setTotalPrice(BigDecimal.valueOf(5000));

        settlementRequestDto.setSettlementDetails(List.of(detail));

        // Mock Repository 동작 정의
        when(settlementRepository.save(any(SettlementEntity.class))).thenAnswer(invocation -> {
            SettlementEntity entity = invocation.getArgument(0);
            entity.setId(1L); // 저장된 엔티티 ID 설정
            return entity;
        });

        // When: 결제 처리 호출
        settlementService.processSettlement(settlementRequestDto);

        // Then: Repository save 호출 검증
        verify(settlementRepository, times(1)).save(any(SettlementEntity.class));

        // 저장된 데이터를 검증 (예: 첫 번째 호출의 파라미터)
        ArgumentCaptor<SettlementEntity> captor = ArgumentCaptor.forClass(SettlementEntity.class);
        verify(settlementRepository).save(captor.capture());
        SettlementEntity savedEntity = captor.getValue();

        // 저장된 결제 정보 검증
        assertEquals(mockStore, savedEntity.getStore());
        assertEquals("Test Menu", savedEntity.getMenu());
        assertEquals(2L, savedEntity.getMenuCount());
        assertEquals(0, savedEntity.getTotalPrice().compareTo(BigDecimal.valueOf(5000))); // BigDecimal 비교
        assertEquals("John Doe", savedEntity.getTakeoutName());
        assertEquals("123-456-7890", savedEntity.getTakeoutPhone());
        assertEquals(0, savedEntity.getTotalAmount().compareTo(BigDecimal.valueOf(10000)));
    }

    @Test
    @DisplayName("하루 매장별 매출 계산 테스트")
    void todaySettlementTest() {
        // given: 테스트에 필요한 데이터 준비
        Long storeId = 1L;
        LocalDateTime localDateTimeOfFrom = LocalDateTime.of(2024, 12, 28, 0, 0, 0, 0);
        LocalDateTime localDateTimeOfTo = LocalDateTime.of(2024, 12, 28, 23, 59, 59, 999999);

        // Mock 데이터 준비
        StoreEntity mockStore = new StoreEntity();
        mockStore.setId(storeId);
        mockStore.setStore("Test Store");

        SettlementEntity settlementEntity1 = new SettlementEntity();
        settlementEntity1.setMenu("Test Menu");
        settlementEntity1.setMenuCount(2L);
        settlementEntity1.setTotalPrice(BigDecimal.valueOf(5000));
        settlementEntity1.setStore(mockStore);

        SettlementEntity settlementEntity2 = new SettlementEntity();
        settlementEntity2.setMenu("Test Menu");
        settlementEntity2.setMenuCount(3L);
        settlementEntity2.setTotalPrice(BigDecimal.valueOf(3000));
        settlementEntity2.setStore(mockStore);

        List<SettlementEntity> settlementEntities = Arrays.asList(settlementEntity1, settlementEntity2);

        // Mocking repository behavior
        when(settlementRepository.findAllByStoreIdAndCreateAtBetween(storeId, localDateTimeOfFrom, localDateTimeOfTo))
                .thenReturn(settlementEntities);
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(mockStore));

        // when: 서비스 메서드 호출
        List<SettlementDto> result = settlementService.todaySettlement(storeId, localDateTimeOfFrom, localDateTimeOfTo);

        // then: 반환된 결과 검증
        assertNotNull(result);
        assertEquals(1, result.size());  // 메뉴는 하나만 존재

        SettlementDto dto = result.get(0);
        assertEquals(mockStore, dto.getStore());
        assertEquals("Test Menu", dto.getMenu());
        assertEquals(5L, dto.getMenuCount());  // 2 + 3 = 5
        assertEquals(BigDecimal.valueOf(19000), dto.getTotalPrice());
        assertEquals(BigDecimal.valueOf(19000), dto.getTodayAmount());  // 하루 총 매출도 19000
    }

    @Test
    @DisplayName("특정 기간 매출 현황 조회 - 정상적으로 매출 정보가 집계된다")
    void periodSettlementMultipleMenuTest() {
        // Given
        Long storeId = 1L;
        LocalDate from = LocalDate.of(2024, 1, 1);
        LocalDate to = LocalDate.of(2024, 1, 31);

        // Mock Store 데이터 생성
        StoreEntity mockStore = new StoreEntity();
        mockStore.setId(storeId);
        mockStore.setStore("Test Store");

        // Mock Settlement 데이터 생성
        List<SettlementEntity> mockSettlements = Arrays.asList(
                createSettlementEntity(mockStore, "아메리카노", 2L, new BigDecimal("4500")),
                createSettlementEntity(mockStore, "아메리카노", 1L, new BigDecimal("4500")),
                createSettlementEntity(mockStore, "카페라떼", 2L, new BigDecimal("5000"))
        );

        // Mock Repository 동작 정의
        when(storeRepository.findById(storeId))
                .thenReturn(Optional.of(mockStore)); // store 정보는 한 번만 조회

        when(settlementRepository.findAllByStoreIdAndCreateAtBetween(
                eq(storeId),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(mockSettlements);

        // When
        List<SettlementDto> result = settlementService.periodSettlement(storeId, to, from);

        // Then
        // 전체 결과 검증
        assertEquals(2, result.size()); // 두 종류의 메뉴가 있어야 함

        // 아메리카노 결과 검증
        SettlementDto americano = result.stream()
                .filter(dto -> dto.getMenu().equals("아메리카노"))
                .findFirst()
                .orElseThrow();

        assertEquals(3L, americano.getMenuCount()); // 총 3잔
        assertEquals(0, americano.getTotalPrice().compareTo(new BigDecimal("13500"))); // 4500 * 3

        // 카페라떼 결과 검증
        SettlementDto latte = result.stream()
                .filter(dto -> dto.getMenu().equals("카페라떼"))
                .findFirst()
                .orElseThrow();

        assertEquals(2L, latte.getMenuCount()); // 총 2잔
        assertEquals(0, latte.getTotalPrice().compareTo(new BigDecimal("10000"))); // 5000 * 2

        // 전체 매출 검증
        BigDecimal expectedTotalAmount = new BigDecimal("23500"); // 13500 + 10000
        assertEquals(0, result.get(0).getTodayAmount().compareTo(expectedTotalAmount));

        // Repository 호출 검증 - times() 메서드로 정확한 호출 횟수 지정
        verify(settlementRepository, times(1)).findAllByStoreIdAndCreateAtBetween(
                eq(storeId),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        );
        verify(storeRepository, times(1)).findById(storeId); // store 조회는 한 번만 호출되어야 함
    }

    private SettlementEntity createSettlementEntity(
            StoreEntity store,
            String menuName,
            Long menuCount,
            BigDecimal price
    ) {
        return SettlementEntity.builder()
                .store(store)
                .menu(menuName)
                .menuCount(menuCount)
                .totalPrice(price)
                .build();
    }

}
