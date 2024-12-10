package zerobase.tableNow.domain.user.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    private String user;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role; //사용자타입
    private Status userStatus; //이용가능한 상태, 정지상태

//    private String name;
//    private String password;
//    private String phone;
}
