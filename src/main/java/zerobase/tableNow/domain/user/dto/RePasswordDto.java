package zerobase.tableNow.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RePasswordDto {
    private String user;
    private String rePassword;
    private String email;
}
