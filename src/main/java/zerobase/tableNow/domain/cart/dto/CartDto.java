package zerobase.tableNow.domain.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private Long id;
    private Long userId;

    @Min(value = 1, message = "메뉴추가는 최소 1개부터 시작")
    private Long menuId;

    private Long storeId;

    @Min(value = 1, message = "개수 최소 1개 이상")
    private int totalCount;//개수

    @Min(value = 100, message = "100원 이상 주문 가능")
    private int totalAmount; // 총액
}
