package zerobase.tableNow.domain.user.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import zerobase.tableNow.domain.user.dto.KakaoLoginResponse;
import zerobase.tableNow.domain.user.entity.UsersEntity;
import zerobase.tableNow.domain.user.repository.UserRepository;
import zerobase.tableNow.domain.user.service.KakaoService;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoServicelmpl implements KakaoService {

    private final UserRepository userRepository;

    @Value("${security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Override
    @Transactional
    /**
     * 1. 프론트에서 받은 code를 활용
     */
    public KakaoLoginResponse processKakaoLogin(String authorizationCode) {
        log.info("권한 인증 요청: {}", authorizationCode);

        // 2. getKakaoAccessToken 메소드에 code를 매개변수로 넘겨주고 카카오 측으로 요청
        Map<String, String> tokenResponse = getKakaoAccessToken(authorizationCode);
        // 4. 요청받은 정보에서 토큰을 꺼냄
        String accessToken = tokenResponse.get("access_token");
        String refreshToken = tokenResponse.get("refresh_token");

        // 5. 꺼내온 accessToken에서 email을 추츨하기 위한 getKakaoUserEmail 메소드 활용
        String userEmail = getKakaoUserEmail(accessToken);

        // 7. 회원테이블에 카카오로 로그인한 정보를 저장
        findOrCreateUser(userEmail, accessToken, refreshToken);

        // 9. 토큰과 이메일 응답
        return new KakaoLoginResponse(accessToken, userEmail);
    }

    /**
     * 3. 카카오 측으로 정보 요청 코드
     * @param authorizationCode 카카오에서 발급받은 code
     * @return 200OK, 카카오 회원 정보
     */
    private Map<String, String> getKakaoAccessToken(String authorizationCode) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", authorizationCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            log.error("Failed to get access token, response: {}", response);
            throw new RuntimeException("Failed to get access token");
        }

        return Map.of(
                "access_token", (String) response.getBody().get("access_token"),
                "refresh_token", (String) response.getBody().get("refresh_token")
        );
    }

    /**
     * 6. 받은 토큰으로 이메일 추출
     * @param accessToken accessToken
     * @return 이메일(userEmail)
     */
    public String getKakaoUserEmail(String accessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(
                userInfoUrl,
                HttpMethod.GET,
                request,
                Map.class
        );

        // 사용자 정보 추출
        Map<String, Object> kakaoAccount = (Map<String, Object>) response.getBody().get("kakao_account");
        if (kakaoAccount != null && kakaoAccount.containsKey("email")) {
            return (String) kakaoAccount.get("email");
        } else {
            throw new RuntimeException("Email not provided by Kakao");
        }
    }

    /**
     * 8. 회원 테이블에 빌드하여 저장
     * @param userEmail userEmail
     * @param accessToken accessToken
     * @param refreshToken refreshToken
     * @return 회원 저장
     */
    private UsersEntity findOrCreateUser(String userEmail, String accessToken, String refreshToken) {
        return userRepository.findByEmail(userEmail)
                .orElseGet(() -> {
                    log.info("No existing user found, creating new user with email: {}", userEmail);
                    UsersEntity newUser = UsersEntity.builder()
                            .email(userEmail)
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build();
                    return userRepository.save(newUser);
                });
    }

    /**
     * 카카오 로그아웃
     * @param accessToken 카카오에서 받은 토큰
     * @return 성공/실패 결과
     */
    @Override
    public ResponseEntity<String> kakaoLogout(String accessToken) {
        try {
            // 카카오 로그아웃 API 호출
            String logoutUrl = "https://kapi.kakao.com/v1/user/logout";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);

            HttpEntity<Void> request = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(
                    logoutUrl,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("Kakao logout success.");
                return ResponseEntity.ok("로그아웃 성공");
            } else {
                log.error("Kakao logout failed: {}", response);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("로그아웃 실패");
            }
        } catch (Exception e) {
            log.error("Kakao logout error:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("내부 서버 오류가 발생했습니다.");
        }
    }
}
