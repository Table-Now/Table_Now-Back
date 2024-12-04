package zerobase.tableNow.domain.store.controller.manager.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.reservation.entity.ReservationEntity;
import zerobase.tableNow.domain.store.controller.manager.dto.ConfirmDto;
import zerobase.tableNow.domain.store.controller.manager.dto.ManagerDto;
import zerobase.tableNow.domain.store.controller.manager.repository.ManagerRepository;
import zerobase.tableNow.domain.store.controller.manager.service.ManagerService;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.store.repository.StoreRepository;
import zerobase.tableNow.domain.user.entity.UsersEntity;
import zerobase.tableNow.domain.user.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {
    private final ManagerRepository managerRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    /**
     * 매니저 전용 상점 목록
     * @param userId
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
     * @param store
     * @return 본인 상점에 대한 예약정보
     */
    @Override
    public List<ConfirmDto> confirmList(String store) {
        List<ReservationEntity> reservations = managerRepository.findByStore_StoreAndReservationStatus(store, Status.ING);

        // 예약 날짜와 시간을 기준으로 오름차순 정렬
        return reservations.stream()
                .sorted(Comparator.comparing(ReservationEntity::getReservationDateTime))
                .map(reservation -> ConfirmDto.builder()
                        .store(reservation.getStore().getStore())
                        .phone(reservation.getPhone())
                        .reservationDateTime(reservation.getReservationDateTime())
                        .peopleNb(reservation.getPeopleNb())
                        .reservationStatus(reservation.getReservationStatus())
                        .build())
                .collect(Collectors.toList());
    }

}
