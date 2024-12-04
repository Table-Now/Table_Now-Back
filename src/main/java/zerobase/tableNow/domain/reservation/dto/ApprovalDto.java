package zerobase.tableNow.domain.reservation.dto;

import lombok.*;
import zerobase.tableNow.domain.constant.Status;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApprovalDto {
    private String phone;
    private Status status; // 현재 상태를 저장하는 필드
    private Boolean reserCheck; // 예약 10분 전 체크 여부

}
