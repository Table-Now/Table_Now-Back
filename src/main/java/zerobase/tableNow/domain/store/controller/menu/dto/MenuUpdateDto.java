package zerobase.tableNow.domain.store.controller.menu.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuUpdateDto {
    private Long id;
    private String image;

    @NotBlank(message = "메뉴 이름은 필수 항목입니다.")
    private String name;

    @NotNull(message = "금액 필수 입력.")
    @Min(value = 100, message = "100원 이상이어야 합니다.")
    private int price;

    private int count;
}
