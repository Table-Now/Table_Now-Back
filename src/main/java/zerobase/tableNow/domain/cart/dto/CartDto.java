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
    @NotBlank(message = "사용자 ID는 필수입니다.")
    private Long userId;

    @NotBlank(message = "메뉴추가 필수입니다.")
    @Min(value = 1, message = "메뉴추가는 최소 1개부터 시작")
    private Long menuId;

    @NotBlank(message = "매장선택 필수입니다.")
    private Long store;

    @Min(value = 1, message = "count는 최소 1 부터 시작")
    private int count = 0;
}
