package zerobase.tableNow.domain.payment.dto;

import lombok.*;
import zerobase.tableNow.domain.constant.PaymentStatus;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.order.entity.OrderEntity;
import zerobase.tableNow.domain.payment.entity.PaymentEntity;

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
    private String merchantUid;
    private PaymentStatus status;
    private BigDecimal totalAmount;
    private String payMethod;
    private String cardName;
    private String cardNumber;
    private Integer cardQuota;
    private String buyerName;
    private String buyerEmail;
    private String buyerTel;

    public static PaymentRequestDto fromEntity(PaymentEntity entity) {
        return PaymentRequestDto.builder()
                .impUid(entity.getImpUid())
                .merchantUid(entity.getMerchantUid())
                .status(entity.getStatus())
                .totalAmount(entity.getTotalAmount())
                .payMethod(entity.getPayMethod())
                .cardName(entity.getCardName())
                .cardNumber(entity.getCardNumber())
                .cardQuota(entity.getCardQuota())
                .buyerName(entity.getBuyerName())
                .buyerEmail(entity.getBuyerEmail())
                .buyerTel(entity.getBuyerTel())
                .build();
    }
}
