package zerobase.tableNow.domain.store.controller.manager.dto;

import lombok.*;
import zerobase.tableNow.domain.store.entity.StoreEntity;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettlementDto {

    private StoreEntity store;
    private String menu;
    private Long menuCount;
    private BigDecimal totalPrice;

    private BigDecimal todayAmount;

}
