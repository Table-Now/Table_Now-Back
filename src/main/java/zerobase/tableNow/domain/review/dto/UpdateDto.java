package zerobase.tableNow.domain.review.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import zerobase.tableNow.domain.user.entity.UsersEntity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDto {
    private Long id;

    @NotBlank(message = "사용자 ID는 필수입니다.")
    private String user;

    private String store; // 상점
    private String contents; // 리뷰내용
}


