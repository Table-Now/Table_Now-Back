package zerobase.tableNow.domain.store.controller.manager.dto;

import lombok.*;
import zerobase.tableNow.domain.store.entity.StoreEntity;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SettlementRequestDto {
    private List<settlementDetail> settlementDetails;
    private String takeoutName;
    private String takeoutPhone;
    private BigDecimal totalAmount;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class settlementDetail{
        private StoreEntity store;
        private String menu;
        private Long menuCount;
        private BigDecimal totalPrice;
    }
}
