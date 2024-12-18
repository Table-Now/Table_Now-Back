package zerobase.tableNow.domain.review.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordRequestDto {
    private Long id;
    @NotBlank(message = "상점 이름 필수 입력.")
    private String user;
    @NotBlank(message = "패스워드 필수 입력.")
    private String password;
}
