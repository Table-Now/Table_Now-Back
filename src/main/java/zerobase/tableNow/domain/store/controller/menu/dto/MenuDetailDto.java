package zerobase.tableNow.domain.store.controller.menu.dto;

import lombok.*;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.store.controller.menu.entity.MenuEntity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuDetailDto {
    private Long id;
    private String name;
    private String image;
    private int price;
    private Status status;
    private int count;

    public static MenuDetailDto of(MenuEntity menu) {
        return MenuDetailDto.builder()
                .id(menu.getId())
                .name(menu.getName())
                .image(menu.getImage())
                .price(menu.getPrice())
                .status(menu.getStatus())
                .count(menu.getCount())
                .build();
    }

}
