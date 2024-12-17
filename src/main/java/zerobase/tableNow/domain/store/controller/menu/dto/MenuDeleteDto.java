package zerobase.tableNow.domain.store.controller.menu.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuDeleteDto {
    private Long id;
    private String image;
    private String name;
    private String price;
}
