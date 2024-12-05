package zerobase.tableNow.domain.store.controller.manager.dto;

import lombok.*;
import zerobase.tableNow.domain.constant.Status;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfirmDto {
    private String store; // 상점이름
    private String phone; // 예약자 번호
    private Integer peopleNb;// 예약인원
}
