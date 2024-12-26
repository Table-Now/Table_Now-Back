package zerobase.tableNow.domain.order.orderDetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDto {
    @JsonProperty("menuId")
    private Long menuId;
    private Long totalPrice;
    private Long menuCount;
}
