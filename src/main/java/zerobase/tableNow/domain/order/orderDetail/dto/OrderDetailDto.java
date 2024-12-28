package zerobase.tableNow.domain.order.orderDetail.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDto {
    private Long menuId;
    private String menu;
    private String store;
    private Long menuCount;
    private BigDecimal totalPrice;

}
