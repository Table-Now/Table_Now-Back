package zerobase.tableNow.domain.reservation.dto;

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
    private String userId;
    private String phone;

    private String store; //상점이름
    private LocalDateTime reserDateTime;//예약 날짜, 시간
    private Integer peopleNb; // 예약인원
    private Status reservationStatus; //예약10분전 체크

}
