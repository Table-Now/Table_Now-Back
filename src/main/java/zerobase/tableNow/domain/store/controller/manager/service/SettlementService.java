package zerobase.tableNow.domain.store.controller.manager.service;

import zerobase.tableNow.domain.store.controller.manager.dto.SettlementRequestDto;
import zerobase.tableNow.domain.store.controller.manager.dto.SettlementDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SettlementService {
    void processSettlement(SettlementRequestDto settlementRequestDto);


    List<SettlementDto> todaySettlement(Long storeId, LocalDateTime localDateTimeOfFrom, LocalDateTime localDateTimeOfTo);

    List<SettlementDto> periodSettlement(Long storeId, LocalDate to, LocalDate from);
}
