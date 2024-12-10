package zerobase.tableNow.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import zerobase.tableNow.domain.reservation.repository.ReservationRepository;
import zerobase.tableNow.domain.reservation.service.impl.ReservationServiceImpl;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfiguration {
    private final ReservationServiceImpl reservationService;
    private final ReservationRepository reservationRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void resetWaitingNumbers(){
        log.info("대기번호 정보 모두 삭제시작 ");
        reservationRepository.deleteAll();
        log.info("대기번호 정보 초기화 완료");

        log.info("대기번호 초기화 작업 시작");
        // ReservationServiceImpl의 메서드를 호출하여 대기번호 초기화
//        reservationService.resetWaitingNumbers();
        log.info("대기번호 초기화 작업 완료");
    }

}
