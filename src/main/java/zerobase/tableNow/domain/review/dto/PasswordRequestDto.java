package zerobase.tableNow.domain.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordRequestDto {
    private Long id;
    private String user;
    private String password;
}
