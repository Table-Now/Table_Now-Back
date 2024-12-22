package zerobase.tableNow.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import zerobase.tableNow.domain.constant.Role;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LocalLoginDto {
    @NotBlank(message = "사용자 ID는 필수입니다.")
    private String user;
    private String phone;

    @NotBlank(message = "비밀번호 필수입니다.")
    private String password;

    private String token;
    private Role role;
}
