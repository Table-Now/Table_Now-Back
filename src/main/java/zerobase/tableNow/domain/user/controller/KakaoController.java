package zerobase.tableNow.domain.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobase.tableNow.domain.user.dto.KakaoLoginResponse;
import zerobase.tableNow.domain.user.service.KakaoService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/kakao/")
public class KakaoController {

    private final KakaoService kakaoService;

    /**
     * 카카오 로그인
     * @param authorizationCode = 권한 인증 코드
     * @return 이메일,토큰
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> kakaoLogin(@RequestParam("code") String authorizationCode) {
        try {
            // Get access token and user email
            KakaoLoginResponse loginResponse = kakaoService.processKakaoLogin(authorizationCode);

            // Prepare response
            Map<String, String> response = new HashMap<>();
            response.put("accessToken", loginResponse.getAccessToken());
            response.put("email", loginResponse.getEmail());

            log.info(response.toString());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Kakao login error:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "내부 서버 오류가 발생했습니다."));
        }
    }

    /**
     * 카카오 로그아웃
     * @param token 카카오에서 받은 토큰
     * @return 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<String> kakaoLogout(@RequestBody Map<String, String> token) {
        String accessToken = token.get("accessToken");
        if (accessToken == null || accessToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Access token is missing.");
        }
        // 서비스 로직 호출
        return kakaoService.kakaoLogout(accessToken);
    }
}
