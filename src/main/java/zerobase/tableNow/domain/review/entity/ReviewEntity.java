package zerobase.tableNow.domain.review.entity;

import jakarta.persistence.*;
import lombok.*;
import zerobase.tableNow.domain.baseEntity.BaseEntity;
import zerobase.tableNow.domain.constant.Role;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.user.entity.UsersEntity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reviews")
public class ReviewEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_id")
    private String store; // 상점

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UsersEntity user;

    private String contents; // 리뷰내용

    @Enumerated(EnumType.STRING)
    private Role role; //사용자타입

    private Boolean secretReview; //비밀글

    @Column(nullable = true)
    private String password; // 비밀글 비밀번호 (암호화된 상태로 저장)
}
