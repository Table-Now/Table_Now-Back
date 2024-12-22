package zerobase.tableNow.domain.payment.dto;

import lombok.*;
import zerobase.tableNow.domain.constant.Status;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private Long id;
    private String userId;
    private String storeId;

    private int totalCount;
    private int totalAmount; // 총액
    private Status status; //매진 여부
}
