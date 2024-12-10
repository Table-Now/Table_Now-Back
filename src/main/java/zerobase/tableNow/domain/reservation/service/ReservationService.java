package zerobase.tableNow.domain.reservation.service;

import zerobase.tableNow.domain.reservation.dto.ApprovalDto;
import zerobase.tableNow.domain.reservation.dto.DeleteDto;
import zerobase.tableNow.domain.reservation.dto.ReservationDto;

import java.util.List;

public interface ReservationService {
    //예약요청
    ReservationDto request(ReservationDto reservationDto);

    //예약취소
    void delete(String store);

    //예약 확정
    ApprovalDto approve(String phone);

    //예약 중인지 확인
    boolean myrelist(String user, Long id);

    // 사용자 상점 예약 목록
    List<ReservationDto> reservationList(String user);



}
