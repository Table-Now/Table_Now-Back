package zerobase.tableNow.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import zerobase.tableNow.domain.constant.Role;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteDto {
    @NotBlank(message = "사용자 ID는 필수입니다.")
    private String user;

    private Role role;
}
