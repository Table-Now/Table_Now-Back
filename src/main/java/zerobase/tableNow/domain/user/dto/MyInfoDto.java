package zerobase.tableNow.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import zerobase.tableNow.domain.constant.Role;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyInfoDto {
    @NotBlank(message = "사용자 ID는 필수입니다.")
    private String user;
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;
    @NotBlank(message = "전화번호는 필수입니다.")
    private String phone;

    private Role role;
    private LocalDateTime createAt;

    //    private String name;
}
