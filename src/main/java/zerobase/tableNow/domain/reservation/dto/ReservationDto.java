package zerobase.tableNow.domain.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import zerobase.tableNow.domain.constant.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {
    private Long id;

    @NotBlank(message = "사용자 ID는 필수 입력.")
    private String userId;
    @NotBlank(message = "전화번호 필수 입력.")
    private String phone;
    @NotBlank(message = "상점 이름 필수 입력.")
    private String store; //상점이름
    @NotBlank(message = "예약인원 필수 입력.")
    private Integer peopleNb; // 예약인원

    private Status reservationStatus; // 줄서기여부

    private Long storeId;
}
