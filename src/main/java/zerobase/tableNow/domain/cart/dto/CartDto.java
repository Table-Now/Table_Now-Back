package zerobase.tableNow.domain.cart.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private Long id;
    private Long userId;
    private Long menuId;
    private Long store;
    private int count = 0;
}
