package zerobase.tableNow.domain.reservation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
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
import zerobase.tableNow.exception.TableException;
import zerobase.tableNow.exception.type.ErrorCode;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
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
    private final Queue<Integer> waitingNumberQueue;
    private final Lock lock = new ReentrantLock(); // 락 객체 생성

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
                .orElseThrow(() -> new TableException(ErrorCode.USER_NOT_FOUND));

        if (!users.getPhone().equals(reservationDto.getPhone())) {
            throw new TableException(ErrorCode.USER_NOT_FOUND,"가입하신 번호와 다른 번호입니다. 확인해주세요");
        }

        StoreEntity store = storeRepository
                .findByStore(reservationDto.getStore())
                .orElseThrow(() -> new TableException(ErrorCode.PRODUCT_NOT_FOUND));

        //유저가 줄서기중  다른 매장 줄서기 등록 X
        boolean isAlreadyQueued = reservationRepository.existsByUserAndStoreNot(users,store);
        if (isAlreadyQueued){
            throw  new TableException(ErrorCode.CONFLICT);
        }

        // 예약 저장
        ReservationEntity reservationEntity = reservationMapper.toReserEntity(reservationDto, users, store);
        ReservationEntity saveEntity = reservationRepository.save(reservationEntity);

        String email = users.getEmail();
        String subject = "TableNow 예약 정보";
        String text = mailComponents.getEmailReservation(reservationDto.getStore());

        mailComponents.sendMail(email, subject, text);

        return reservationMapper.toReserDto(saveEntity);
    }

    //예약 취소
    @Transactional
    @Override
    public void delete(String store) {
        // 현재 로그인한 사용자 정보 가져오기
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        ReservationEntity reservation = reservationRepository.findByUser_User(userId);

        if (reservation.getStore().getStore().equals(store)) {
            // 상점이 동일하면 예약 삭제
            reservationRepository.delete(reservation);
        }

    }

    // 예약확정
    @Override
    public ApprovalDto approve(String phone) {
        lock.lock();
        try {
            // 해당 전화번호로 예약 엔티티를 조회
            ReservationEntity reservationEntity = reservationRepository.findByPhone(phone)
                    .orElseThrow(() -> new TableException(ErrorCode.USER_NOT_FOUND,"해당 번호가 없습니다."));

            // 상태가 ING(진행 중)인 경우에만 대기번호를 부여
            if (reservationEntity.getReservationStatus() == Status.ING) {
                // 대기번호 부여 (큐에서 하나씩 꺼내서 부여)
                Integer waitingNumber =
                        waitingNumberQueue.isEmpty() ? 1 : waitingNumberQueue.poll(); // 큐에서 대기번호를 꺼내고, 다음 번호 부여

                // 다음 대기번호를 큐에 추가
                waitingNumberQueue.add(waitingNumber + 1);

                // 줄서기 상태는 STOP으로 변경
                reservationEntity.setReservationStatus(Status.STOP);
                reservationEntity.setWaitingNumber(waitingNumber);  // 대기번호 설정
                reservationRepository.save(reservationEntity);  // 변경된 상태 저장

                // ApprovalDto 응답 객체 생성
                return new ApprovalDto("대기가 확정되었습니다. 대기번호 : " + waitingNumber);
            } else {
                // 이미 대기 상태가 아니면 예외 처리
                throw new TableException(ErrorCode.INVALID_REQUEST,"이미 대기 상태가 아닙니다");
            }
        } finally {
            lock.unlock();  // 락 해제
        }
    }
    public void resetWaitingNumbers() {
        // 큐를 비우고, 대기번호를 1로 리셋
        waitingNumberQueue.clear();
        waitingNumberQueue.add(1); // 첫 번째 대기번호를 1로 설정
        log.info("대기번호가 초기화되었습니다.");
    }



    // 사용자 상점 예약 리스트 목록
    @Override
    public List<ReservationDto> reservationList(String user) {
        UsersEntity userId = userRepository.findByUser(user)
                .orElseThrow(() -> new TableException(ErrorCode.USER_NOT_FOUND));

        List<ReservationEntity> entities = reservationRepository.findByUser(userId);

        return entities.stream()
                .map(reservation -> {
                    // storeId를 추가로 포함시켜 ReservationDto로 변환
                    ReservationDto dto = reservationMapper.toReserDto(reservation);
                    dto.setStoreId(reservation.getStore().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }


    // 예약중인지 확인
    @Override
    public boolean myrelist(String user, Long id) {
        log.info("Input user: {}", user); // 사용자 이름 로깅
        log.info("Input id: {}", id); // 예약 ID 로깅

        // StoreEntity를 DB에서 조회
        StoreEntity store = storeRepository.findById(id).orElse(null);
        if (store == null) {
            log.warn("Store with id {} not found", id);
            return false;
        }

        boolean exists = reservationRepository.existsByStore(store);

        log.info("Query result for user '{}' and id '{}': {}", user, id, exists); // 쿼리 결과 로깅
        return exists;
    }
}



