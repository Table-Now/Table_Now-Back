package zerobase.tableNow.domain.store.controller.manager.dto;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfirmDto {
    private String store; // 상점이름
    private String user; // 예약자 이름
    private String phone; // 예약자 번호
    private Integer peopleNb;// 예약인원
    private Integer waitingNumber; // 대기번호 추가
}
