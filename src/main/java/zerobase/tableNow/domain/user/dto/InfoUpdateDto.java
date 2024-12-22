package zerobase.tableNow.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import zerobase.tableNow.domain.constant.Status;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InfoUpdateDto {
    @NotBlank(message = "전화번호는 필수입니다.")
    private String phone;
    private String password;
    private String email;
    private Status status;
}
