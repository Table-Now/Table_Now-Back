package zerobase.tableNow.domain.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import zerobase.tableNow.domain.constant.Status;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApprovalDto {
    private Long id;
    @NotBlank(message = "전화번호 필수 입력.")
    private String phone;

    private Status reservationStatus;
    private String message; // 대기번호에 대한 메시지

    // String 메시지만 받는 생성자 추가
    public ApprovalDto(String message) {
        this.message = message;
    }
}
