package zerobase.tableNow.domain.order.dto;

import lombok.*;
import zerobase.tableNow.domain.constant.PayMethod;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private String userId;
    private Long storeId;
    private String orderName;
    private String merchantUid;
    private BigDecimal totalAmount;
    private Boolean paymentStatus;
    private PayMethod payMethod;
}
