package zerobase.tableNow.domain.order.dto;

import lombok.*;
import zerobase.tableNow.domain.order.orderDetail.dto.OrderDetailDto;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private String user;
    private String takeoutName;
    private String takeoutPhone;
    private BigDecimal totalAmount;
    private String payMethod;
    private String uuid;
    private List<OrderDetailDto> orderDetails;
}
