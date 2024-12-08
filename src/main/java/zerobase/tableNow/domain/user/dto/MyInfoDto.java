package zerobase.tableNow.domain.user.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyInfoDto {
    private String user;
//    private String name;
    private String email;
//    private String phone;
    private LocalDateTime createAt;
}
