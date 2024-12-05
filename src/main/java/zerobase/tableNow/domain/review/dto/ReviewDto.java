package zerobase.tableNow.domain.review.dto;

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
    private String user;
    private String store; // 상점
    private String contents; // 리뷰내용
    private Role role;
    private Boolean secretReview;
    private String password; // 비밀번호 (비밀리뷰일 때만 저장)

}
