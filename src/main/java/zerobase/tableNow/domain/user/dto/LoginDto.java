package zerobase.tableNow.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import zerobase.tableNow.domain.constant.Role;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginDto {
    @NotBlank(message = "사용자 ID는 필수입니다.")
    private String user;
    @NotBlank(message = "전화번호는 필수입니다.")
    private String phone;

    private Role role;

    //    private String token;
}
