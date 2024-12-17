package zerobase.tableNow.domain.store.controller.menu.dto;

import jakarta.persistence.*;
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
    private Long storeId;
    private String image;
    private String name;
    private String price;

    @Enumerated(EnumType.STRING)
    private Status status;
}
