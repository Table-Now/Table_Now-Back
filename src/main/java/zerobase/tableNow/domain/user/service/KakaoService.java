package zerobase.tableNow.domain.user.service;

import org.springframework.http.ResponseEntity;
import zerobase.tableNow.domain.user.dto.KakaoLoginResponse;

public interface KakaoService {

    /**
     * 카카오 로그인
     * @param authorizationCode = 권한 인증 코드
     * @return 이메일,토큰
     */
    KakaoLoginResponse processKakaoLogin(String authorizationCode);

    /**
     * 카카오 로그아웃
     * @param accessToken 카카오에서 받은 토큰
     * @return 로그아웃
     */
    ResponseEntity<String> kakaoLogout(String accessToken);
}
