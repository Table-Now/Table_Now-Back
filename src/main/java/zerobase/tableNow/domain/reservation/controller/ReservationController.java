package zerobase.tableNow.domain.reservation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobase.tableNow.domain.reservation.dto.ApprovalDto;
import zerobase.tableNow.domain.reservation.dto.DeleteDto;
import zerobase.tableNow.domain.reservation.dto.ReservationDto;
import zerobase.tableNow.domain.reservation.service.ReservationService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation/")
public class ReservationController {
    private final ReservationService reservationService;

    //예약요청
    @PostMapping("request")
    public ResponseEntity<ReservationDto> request(@RequestBody ReservationDto reservationDto){
        log.info("컨트롤러 요청 등록 + {}", reservationDto);
        return ResponseEntity.ok().body(reservationService.request(reservationDto));
    }

    //예약 취소
    @DeleteMapping("delete")
    public ResponseEntity<Void> delete(@RequestParam("id") Long id){
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //예약 확정
    @PostMapping("approval")
    public ResponseEntity<ApprovalDto> approval(@RequestParam(name = "phone") String phone) {
        log.info("요청 폰 번호 + {}", phone);
        ApprovalDto response = reservationService.approve(phone);
        return ResponseEntity.ok(response);
    }

    // 상점 예약 체크
    @GetMapping("myrelist")
    public ResponseEntity<Boolean> myrelist(
            @RequestParam(name = "user") String user,
            @RequestParam(name = "id") Long id
    ) {
        boolean reservation = reservationService.myrelist(user,id);
        return ResponseEntity.ok().body(reservation);
    }

    //내 예약 목록
    @GetMapping("reserlist")
    public ResponseEntity<List<ReservationDto>> reservationList(@RequestParam(name = "user") String user) {
        return ResponseEntity.ok().body(reservationService.reservationList(user));
    }

}
