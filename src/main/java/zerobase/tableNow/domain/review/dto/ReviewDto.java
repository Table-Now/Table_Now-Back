package zerobase.tableNow.domain.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import zerobase.tableNow.domain.constant.Role;
import zerobase.tableNow.domain.user.entity.UsersEntity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto{
    private Long id;
    @NotBlank(message = "사용자 ID는 필수 입력.")
    private String user;
    @NotBlank(message = "상점 이름 필수 입력.")
    private String store; // 상점
    @Size(max = 100,message = "최대 100자 입력 가능")
    private String contents; // 리뷰내용

    private Role role;
    @Size(max = 100,message = "최대 100자 입력 가능")
    private Boolean secretReview;
    private String password; // 비밀번호 (비밀리뷰일 때만 저장)

}
