package zerobase.tableNow.domain.user.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import zerobase.tableNow.domain.constant.Role;
import zerobase.tableNow.domain.constant.Status;

import java.time.LocalDateTime;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegisterDto {
    @NotBlank(message = "사용자 ID는 필수입니다.")
    private String user;
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;
    private String password;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role; //사용자타입
    private Status userStatus; //이용가능한 상태, 정지상태
}
