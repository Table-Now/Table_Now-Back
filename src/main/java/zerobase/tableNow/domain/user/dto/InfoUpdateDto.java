package zerobase.tableNow.domain.user.dto;

import lombok.*;
import zerobase.tableNow.domain.constant.Status;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InfoUpdateDto {
    private String phone;
}
