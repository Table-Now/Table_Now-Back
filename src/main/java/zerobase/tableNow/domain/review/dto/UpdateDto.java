package zerobase.tableNow.domain.review.dto;

import lombok.*;
import zerobase.tableNow.domain.user.entity.UsersEntity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDto {
    private Long id;
    private String user;
    private String store; // 상점
    private String contents; // 리뷰내용
}


