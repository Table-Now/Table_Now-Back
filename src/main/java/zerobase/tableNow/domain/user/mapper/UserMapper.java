//package zerobase.tableNow.domain.user.mapper;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import zerobase.tableNow.domain.constant.Role;
//import zerobase.tableNow.domain.constant.Status;
//import zerobase.tableNow.domain.user.dto.InfoUpdateDto;
//import zerobase.tableNow.domain.user.dto.LoginDto;
//import zerobase.tableNow.domain.user.dto.RegisterDto;
//import zerobase.tableNow.domain.user.entity.UsersEntity;
//
//import java.util.UUID;
//
//@RequiredArgsConstructor
//@Component
//public class UserMapper {
////    private final PasswordEncoder passwordEncoder;
//
//    // 회원가입 DTO -> Entity
//    public UsersEntity toEntity(RegisterDto dto) {
////        String hashPassword = passwordEncoder.encode(dto.getPassword());
//
//        return UsersEntity.builder()
//                .user(dto.getUser())
////                .name(dto.getName())
////                .password(hashPassword)
//                .email(dto.getEmail())
////                .phone(dto.getPhone())
//                .role(Role.USER)
//                .userStatus(Status.REQ)  // 초기 상태는 '요청'
//                .build();
//    }
//
//    // 회원가입 Entity -> Dto
//    public RegisterDto toDto(UsersEntity entity) {
//        return RegisterDto.builder()
//                .user(entity.getUser())
////                .name(entity.getName())
////                .password(entity.getPassword())
//                .email(entity.getEmail())
////                .phone(entity.getPhone())
//                .role(Role.USER)
//                .userStatus(entity.getUserStatus())
//                .build();
//    }
//
//    // 로그인 Entity -> Dto
//    public LoginDto toLoginDto(UsersEntity entity){
//        return LoginDto.builder()
//                .user(entity.getUser())
//                .role(entity.getRole())
//                .build();
//    }
//
//}
//
