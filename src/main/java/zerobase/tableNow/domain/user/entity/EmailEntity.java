//package zerobase.tableNow.domain.user.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import zerobase.tableNow.domain.constant.Status;
//
//import java.time.LocalDateTime;
//
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@Entity
//@Table(name = "EmailAuth")
//public class EmailEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "email")
//    private UsersEntity email;
//
//    @Builder.Default
//    private boolean emailAuthYn = false; //메일 인증 했는지
//    private LocalDateTime emailAuthDt; //이메일 인증 날짜
//    private  String emailAuthKey; // 회원가입할때 생성해서 메일인증할때 쓰는 Key
//    private Status userStatus; //이용가능한 상태, 정지상태
//}
