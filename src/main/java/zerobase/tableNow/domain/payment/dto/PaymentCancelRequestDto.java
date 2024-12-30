package zerobase.tableNow.domain.payment.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCancelRequestDto {
    private String impUid;           // 포트원 결제 고유 ID

}

