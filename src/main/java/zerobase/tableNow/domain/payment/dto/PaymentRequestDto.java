package zerobase.tableNow.domain.payment.dto;

import lombok.*;
import zerobase.tableNow.domain.constant.PaymentStatus;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.order.entity.OrderEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {
    private String impUid;
    private BigDecimal totalAmount;
    private PaymentStatus status;
}
