package zerobase.tableNow.domain.store.controller.manager.service.impl;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zerobase.tableNow.domain.reservation.entity.ReservationEntity;
import zerobase.tableNow.domain.reservation.repository.ReservationRepository;
import zerobase.tableNow.domain.store.controller.manager.dto.ConfirmDto;
import zerobase.tableNow.domain.store.controller.manager.dto.ManagerDto;
import zerobase.tableNow.domain.store.controller.manager.service.ManagerService;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.store.repository.StoreRepository;
import zerobase.tableNow.domain.user.entity.UsersEntity;
import zerobase.tableNow.domain.user.repository.UserRepository;
import zerobase.tableNow.exception.TableException;
import zerobase.tableNow.exception.type.ErrorCode;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManagerServiceImpl implements ManagerService {
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final ReservationRepository reservationRepository;

    /**
     * 매니저 전용 상점 목록
     * @param user user
     * @return 해당 본인 상점에 대한 목록 반환
     */
    @Override
    public List<ManagerDto> managerList(String user) {
        UsersEntity userId = userRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        List<StoreEntity> storeEntities = storeRepository.findByUser(userId);

        // 조회된 상점 목록을 ManagerDto 리스트로 변환
        return storeEntities.stream().map(store -> ManagerDto.builder()
                .id(store.getId())
                .user(userId.getUser())
                .store(store.getStore())
                .storeLocation(store.getStoreLocation())
                .storeImg(store.getStoreImg())
                .storeContents(store.getStoreContents())
                .rating(store.getRating())
                .storeOpen(store.getStoreOpen())
                .storeClose(store.getStoreClose())
                .storeWeekOff(store.getStoreWeekOff())
                .build()
        ).collect(Collectors.toList());
    }


    /**
     * 매니저 전용 예약 정보 확인
     * @param store store
     * @return 본인 상점에 대한 예약정보
     */
    @Transactional(readOnly = true)
    public List<ConfirmDto> getWaitingList(String store) {
        // Store 존재 여부 확인
        storeRepository.findByStore(store).orElseThrow(() -> new TableException(ErrorCode.PRODUCT_NOT_FOUND));

        // 예약 목록 확인
        List<ReservationEntity> reservationEntityList = reservationRepository.findByStore_Store(store);

        // 매핑 및 반환
        List<ConfirmDto> waitingList = reservationEntityList.stream()
                .sorted(Comparator.comparingInt(ReservationEntity::getWaitingNumber))
                .map(reservation -> ConfirmDto.builder()
                        .store(reservation.getStore().getStore())
                        .phone(reservation.getPhone())
                        .peopleNb(reservation.getPeopleNb())
                        .waitingNumber(reservation.getWaitingNumber())
                        .build())
                .collect(Collectors.toList());

        return waitingList;
    }


}
