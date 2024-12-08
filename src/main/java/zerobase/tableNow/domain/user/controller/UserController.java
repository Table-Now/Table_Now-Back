package zerobase.tableNow.domain.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobase.tableNow.domain.user.dto.*;
import zerobase.tableNow.domain.user.service.UserService;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/")
public class UserController {
    private  final UserService userService;

//    /**
//     * 회원 가입
//     * @param registerDto 회원가입 요청 데이터
//     * @return 회원 정보
//     */
//    @PostMapping("register")
//    public ResponseEntity<String> register(
//            @RequestBody RegisterDto registerDto){
//        userService.register(registerDto);
//        return ResponseEntity.ok("회원가입이 완료되었습니다.");
//    }

    // 이메일 인증
    @GetMapping("email-auth")
    public ResponseEntity<?> emailAuth(@RequestParam(name = "user") String user,
                                       @RequestParam(name = "key") String authKey) {
        boolean result = userService.emailAuth(user, authKey);

        if (result) {
            return ResponseEntity.ok().body(Map.of(
                    "message", "이메일 인증이 완료되었습니다.",
                    "success", true
            ));
        }

        return ResponseEntity.badRequest().body(Map.of(
                "message", "이메일 인증에 실패했습니다.",
                "success", false
        ));
    }

    //로그인
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
//        try {
//            // 로그인 성공 시 JWT 토큰 포함된 LoginDto 반환
//            LoginDto responseDto = userService.login(loginDto);
//            return ResponseEntity.ok(responseDto);
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("로그인 실패: " + e.getMessage());
//        }
//    }

    //회원 수정
    @PatchMapping("infoupdate")
    public ResponseEntity<InfoUpdateDto> infoUpdate(@RequestBody InfoUpdateDto infoUpdateDto){
        return ResponseEntity.ok(userService.infoUpdate(infoUpdateDto));
    }

    //회원 정지
    @DeleteMapping("delete")
    public ResponseEntity<DeleteDto> userDelete(@RequestParam(name = "user") String user ){

        return ResponseEntity.ok().body(userService.userDelete(user));
    }

    //내 정보 가져오기
    @GetMapping("/myinfo")
    public ResponseEntity<MyInfoDto> myInfo(@RequestParam(name= "user") String user){
        return ResponseEntity.ok().body(userService.myInfo(user));
    }

}

