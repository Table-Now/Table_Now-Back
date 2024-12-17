package zerobase.tableNow.domain.store.controller.menu.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import zerobase.tableNow.domain.constant.Status;

@Getter
@Setter
public class MenuStatusDto {
    private Long id;
    private Status status;
}
