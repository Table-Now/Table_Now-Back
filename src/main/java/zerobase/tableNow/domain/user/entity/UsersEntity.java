package zerobase.tableNow.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.joda.time.LocalDateTime;
import zerobase.tableNow.domain.baseEntity.BaseEntity;
import zerobase.tableNow.domain.constant.Role;
import zerobase.tableNow.domain.constant.Status;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Users")
public class UsersEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String user;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role; //사용자 타입

    private Status userStatus; //이용가능한 상태, 정지상태

    @Column(nullable = false)
    private Boolean isQueueRestricted;  // 줄서기 금지 상태
    private LocalDateTime queueRestrictionEndTime;  // 줄서기 금지 종료 시간

    private String kakaoAccessToken;
    private String kakaoRefreshToken;


//    private String name;
//    private String password;

}
