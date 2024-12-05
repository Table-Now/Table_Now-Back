package zerobase.tableNow.domain.reservation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.tableNow.components.MailComponents;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.reservation.dto.ApprovalDto;
import zerobase.tableNow.domain.reservation.dto.ReservationDto;
import zerobase.tableNow.domain.reservation.entity.ReservationEntity;
import zerobase.tableNow.domain.reservation.mapper.ReservationMapper;
import zerobase.tableNow.domain.reservation.repository.ReservationRepository;
import zerobase.tableNow.domain.reservation.service.ReservationService;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.store.repository.StoreRepository;
import zerobase.tableNow.domain.user.entity.UsersEntity;
import zerobase.tableNow.domain.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final ReservationMapper reservationMapper;
    private final MailComponents mailComponents;

    /**
     * 예약 요청
     *
     * @param reservationDto
     * @return
     */
    @Override
    public ReservationDto request(ReservationDto reservationDto) {
        log.info("서비스 요청 등록 + {}", reservationDto);
        // 사용자와 매장 정보 조회
        UsersEntity users = userRepository
                .findByUser(reservationDto.getUserId())
                .orElseThrow(() -> new RuntimeException("해당 ID가 없습니다."));

        if (!users.getPhone().equals(reservationDto.getPhone())) {
            throw new RuntimeException("가입하신 번호와 다른 번호입니다. 확인해주세요");
        }

        StoreEntity store = storeRepository
                .findByStore(reservationDto.getStore())
                .orElseThrow(() -> new RuntimeException("해당 가게가 없습니다."));

        // 영업시간 체크
        validateBusinessHours(store, reservationDto.getReservationDateTime());

        // 휴무일 체크
        validateStoreHoliday(store, reservationDto.getReservationDateTime());

        // 예약 저장
        ReservationEntity reservationEntity = reservationMapper.toReserEntity(reservationDto, users, store);
        ReservationEntity saveEntity = reservationRepository.save(reservationEntity);

        String email = users.getEmail();
        String subject = "TableNow 예약 정보";
        String text = mailComponents.getEmailReservation(reservationDto.getStore(), reservationDto.getReservationDateTime());

        mailComponents.sendMail(email, subject, text);

        return reservationMapper.toReserDto(saveEntity);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        //대기 2팀 남았을때 취소 할 경우 2일동안 해당 매장 줄서기 금지

        reservationRepository.deleteById(id);
    }

    // 영업시간 검증
    private void validateBusinessHours(StoreEntity store, LocalDateTime reservationTime) {
        LocalTime reservationLocalTime = reservationTime.toLocalTime();

        // 매장 영업시간 파싱 (HH:mm 형식 가정)
        LocalTime openTime = LocalTime.parse(store.getStoreOpen());
        LocalTime closeTime = LocalTime.parse(store.getStoreClose());

        if (reservationLocalTime.isBefore(openTime) ||
                reservationLocalTime.isAfter(closeTime)) {
            throw new RuntimeException(
                    String.format("영업시간 외 예약입니다. 영업시간: %s ~ %s",
                            store.getStoreOpen(),
                            store.getStoreClose())
            );
        }
    }

    // 휴무일 검증
    private void validateStoreHoliday(StoreEntity store, LocalDateTime reservationTime) {
        String dayOfWeek = reservationTime.getDayOfWeek()
                .getDisplayName(TextStyle.SHORT, Locale.KOREAN);

        if (store.getStoreWeekOff().contains(dayOfWeek)) {
            throw new RuntimeException("해당 날짜는 매장 휴무일입니다.");
        }
    }

    // 사용자 상점 예약 리스트 목록
    @Override
    public List<ReservationDto> reservationList(String user) {
        UsersEntity userId = userRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("ID가 존재하지 않습니다."));

        List<ReservationEntity> entities = reservationRepository.findByUser(userId);

        return entities.stream()
                .map(reservationMapper::toReserDto)
                .collect(Collectors.toList());
    }


    /**
     * 예약확정
     * @param phone
     * @return 예약목록
     */
//    @Override
//    public ApprovalDto approve(String phone) {
//        Optional<ReservationEntity> optionalReservation = reservationRepository.findByPhone(phone);
//        log.info("서비스 예약 번호 확인 + {}", optionalReservation);
//        if (optionalReservation.isEmpty()) {
//            throw new IllegalArgumentException("예약 정보를 찾을 수 없습니다.");
//        }
//
//        ReservationEntity reservation = optionalReservation.get();
//        log.info("예약 내용 확인  + {}", reservation);
//
//        LocalDateTime reservationTime = reservation.getReservationDateTime();
//        LocalDateTime currentTime = LocalDateTime.now();
//        LocalDateTime cutoffTime = reservationTime.minusMinutes(10);
//
//        // 예약 시간 10분 전까지는 ING, 그 이후에는 STOP
//        Status status;
//        if (currentTime.isBefore(cutoffTime)) {
//            status = Status.ING;
//        } else {
//            status = Status.STOP;
//        }
//
//        reservation.setReservationStatus(status);
//        reservationRepository.save(reservation);
//
//        return new ApprovalDto(phone, status, currentTime.isBefore(cutoffTime));
//    }

    // 예약중인지 확인
    @Override
    public boolean myrelist(String user, Long id) {
        return reservationRepository.existsByUserUserAndId(user, id);
    }




}



