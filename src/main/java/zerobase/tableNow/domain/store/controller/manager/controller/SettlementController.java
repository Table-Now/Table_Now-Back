package zerobase.tableNow.domain.store.controller.manager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobase.tableNow.domain.store.controller.manager.dto.SettlementRequestDto;
import zerobase.tableNow.domain.store.controller.manager.dto.SettlementDto;
import zerobase.tableNow.domain.store.controller.manager.service.SettlementService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(name = "/api/settlement")
public class SettlementController {
    private final SettlementService settlementService;

    //실 결제 저장
    @PostMapping("/process")
    public ResponseEntity<String> processSettlement(@RequestBody SettlementRequestDto settlementRequestDto) {
        settlementService.processSettlement(settlementRequestDto);
        return ResponseEntity.ok("OK");
    }

    // 하루 매장별 매출
    @GetMapping("/today/{storeId}")
    private ResponseEntity<List<SettlementDto>> todaySettlement(@Valid @PathVariable(name = "storeId") Long storeId) {
        LocalDate today = LocalDate.now();
        return ResponseEntity.ok().body(settlementService.todaySettlement(storeId, getLocalDateTimeOfFrom(today), getLocalDateTimeOfTo(today)));
    }

    private LocalDateTime getLocalDateTimeOfTo(LocalDate localDate) {
        return LocalDateTime.of(localDate, LocalTime.MAX);
    }

    private LocalDateTime getLocalDateTimeOfFrom(LocalDate localDate) {
        return LocalDateTime.of(localDate, LocalTime.MIN);
    }

    // 특정 기간별 매출 현황
    @GetMapping("/period")
    private ResponseEntity<List<SettlementDto>> periodSettlement(
            @RequestParam(name = "storeId") Long storeId,
            @RequestParam(name = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to,
            @RequestParam(name = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from) {
        return ResponseEntity.ok().body(settlementService.periodSettlement(storeId,to,from));
    }
}
