package zerobase.tableNow.domain.user.dto;

import lombok.*;
import zerobase.tableNow.domain.constant.Role;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyInfoDto {
    private String user;
    private String email;
    private String phone;
    private Role role;
    private LocalDateTime createAt;

    //    private String name;
}
