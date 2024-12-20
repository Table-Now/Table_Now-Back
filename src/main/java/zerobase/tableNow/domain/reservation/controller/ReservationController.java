package zerobase.tableNow.domain.reservation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import zerobase.tableNow.domain.reservation.dto.ApprovalDto;
import zerobase.tableNow.domain.reservation.dto.DeleteDto;
import zerobase.tableNow.domain.reservation.dto.ReservationDto;
import zerobase.tableNow.domain.reservation.service.ReservationService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/reservations/")
public class ReservationController {
    private final ReservationService reservationService;

    //예약요청
    @PostMapping("reservation/create")
    public ResponseEntity<ReservationDto> request(@Valid @RequestBody ReservationDto reservationDto){
        return ResponseEntity.ok().body(reservationService.request(reservationDto));
    }

    //예약 취소
    @DeleteMapping("{store}")
    public ResponseEntity<Void> delete(@PathVariable("store") String store){
        reservationService.delete(store);
        return ResponseEntity.noContent().build();
    }

    //예약 확정
    @PostMapping("{phone}/approval")
    public ResponseEntity<ApprovalDto> approval(@Valid @PathVariable(name = "phone") String phone) {
        log.info("요청 폰 번호 + {}", phone);
        ApprovalDto response = reservationService.approve(phone);
        return ResponseEntity.ok(response);
    }

    // 상점 예약 체크
    @GetMapping("{id}/check")
    public ResponseEntity<Boolean> myrelist(
            @RequestParam(name = "user") String user,
            @PathVariable(name = "id") Long id
    ) {
        boolean reservation = reservationService.myrelist(user,id);
        return ResponseEntity.ok().body(reservation);
    }

    //내 예약 목록
    @GetMapping("{user}/list")
    public ResponseEntity<List<ReservationDto>> reservationList(@PathVariable(name = "user") String user) {
        return ResponseEntity.ok().body(reservationService.reservationList(user));
    }

}
