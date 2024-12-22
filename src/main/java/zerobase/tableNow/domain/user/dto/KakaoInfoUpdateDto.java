package zerobase.tableNow.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KakaoInfoUpdateDto {
    @NotBlank(message = "전화번호는 필수입니다.")
    private String phone;
}
