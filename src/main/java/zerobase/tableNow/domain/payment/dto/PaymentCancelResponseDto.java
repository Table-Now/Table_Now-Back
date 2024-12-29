package zerobase.tableNow.domain.payment.dto;

import lombok.*;
import zerobase.tableNow.domain.constant.PaymentStatus;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCancelResponseDto {
    private String impUid;
    private PaymentStatus status;
    private String message;
}

