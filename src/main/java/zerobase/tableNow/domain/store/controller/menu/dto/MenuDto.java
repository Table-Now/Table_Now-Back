package zerobase.tableNow.domain.store.controller.menu.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.store.entity.StoreEntity;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuDto {
    private Long id;
    @NotBlank(message = "상점 이름 필수 입력.")
    private Long storeId;
    private String image;
    @NotNull(message = "메뉴 이름은 필수 항목입니다.")
    private String name;

    @NotBlank(message = "금액 필수 입력.")
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private String price;

    @Enumerated(EnumType.STRING)
    private Status status;
}
