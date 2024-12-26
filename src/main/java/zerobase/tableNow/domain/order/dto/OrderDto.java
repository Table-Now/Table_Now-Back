package zerobase.tableNow.domain.order.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import zerobase.tableNow.domain.constant.PayMethod;
import zerobase.tableNow.domain.order.orderDetail.OrderDetailDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDto {
//    private Long id;

    // JSON으로 넘어오는 데이터를 파싱하기 위해 설정(안해도 무방)
    @JsonProperty("totalAmount")
    private Long totalAmount;

    @JsonProperty("payMethod")
    private String payMethod;

    @JsonProperty("orderDetails")
    private List<OrderDetailDto> orderDetails = new ArrayList<>();

//    @JsonProperty("user")
//    private String user;


//    private Long storeId;
//    private String orderName;
//    private String merchantUid;
//    private Boolean paymentStatus;
//    private PayMethod payMethod;
}
