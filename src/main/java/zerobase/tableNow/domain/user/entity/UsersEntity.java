package zerobase.tableNow.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import zerobase.tableNow.domain.baseEntity.BaseEntity;
import zerobase.tableNow.domain.constant.Role;
import zerobase.tableNow.domain.constant.Status;

import java.time.LocalDateTime;

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

    @Column(unique = true)
    private String user;
    private String name;
    private String password;


    private String email;
    private String phone;

    @Enumerated(EnumType.STRING) // 또는 EnumType.ORDINAL
    private Role role; //사용자 타입
    private Status userStatus; //이용가능한 상태, 정지상태

    private String kakaoAccessToken;
    private String kakaoRefreshToken;

}
