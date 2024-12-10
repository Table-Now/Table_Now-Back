package zerobase.tableNow.domain.user.dto;

import lombok.*;
import zerobase.tableNow.domain.constant.Role;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginDto {
    private String user;
    private Role role;
    private String phone;

    //    private String token;
}
