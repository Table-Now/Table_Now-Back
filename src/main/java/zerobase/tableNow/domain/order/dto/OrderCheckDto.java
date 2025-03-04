package zerobase.tableNow.domain.order.dto;

import lombok.*;
import zerobase.tableNow.domain.constant.PaymentStatus;
import zerobase.tableNow.domain.order.orderDetail.dto.OrderDetailDto;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCheckDto {
    private String takeoutName;
    private String takeoutPhone;
    private BigDecimal totalAmount;
    private String payMethod;
    private String impUid;
    private PaymentStatus status;
    private List<OrderDetailDto> orderDetails;
}
