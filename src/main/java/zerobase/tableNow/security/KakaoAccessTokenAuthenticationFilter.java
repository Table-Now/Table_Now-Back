//package zerobase.tableNow.security;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.filter.OncePerRequestFilter;
//import zerobase.tableNow.domain.user.entity.UsersEntity;
//import zerobase.tableNow.domain.user.repository.UserRepository;
//import zerobase.tableNow.exception.TableException;
//import zerobase.tableNow.exception.type.ErrorCode;
//
//import java.io.IOException;
//import java.util.Map;
//
//import static org.springframework.util.StringUtils.hasText;
//
//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class KakaoAccessTokenAuthenticationFilter extends OncePerRequestFilter {
//
//    private final UserRepository userRepository;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws IOException, ServletException {
//
//        try {
//            // 요청에서 카카오 액세스 토큰 가져오기
//            String kakaoAccessToken = getJwtFromRequest(request);
//
//            if (hasText(kakaoAccessToken)) {
//                // 카카오 액세스 토큰으로 사용자 정보 조회
//                String userEmail = getKakaoUserEmail(kakaoAccessToken);
//                UsersEntity userEntity = userRepository.findByEmail(userEmail)
//                        .orElseThrow(()-> new TableException(ErrorCode.USER_NOT_FOUND));
//
//                if (userEntity != null) {
//                    // 사용자 이메일로 인증 처리
//                    UserDetails userDetails = new CustomUserDetails(userEmail);
//
//                    UsernamePasswordAuthenticationToken authentication =
//                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//                    // SecurityContext에 인증 정보를 설정
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                }
//            }
//        } catch (Exception ex) {
//            log.error("Could not set user authentication in security context", ex);
//        }
//
//        // 필터 체인 다음 필터로 전달
//        filterChain.doFilter(request, response);
//    }
//
//    // 카카오 액세스 토큰을 Request 헤더에서 가져오는 메서드
//    private String getJwtFromRequest(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization");
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
//
//
//    // 카카오 이메일을 얻는 메서드
//    private String getKakaoUserEmail(String accessToken) {
//        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + accessToken);
//
//        HttpEntity<Void> request = new HttpEntity<>(headers);
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<Map> response = restTemplate.exchange(
//                userInfoUrl,
//                HttpMethod.GET,
//                request,
//                Map.class
//        );
//
//        // 사용자 정보 추출
//        Map<String, Object> kakaoAccount = (Map<String, Object>) response.getBody().get("kakao_account");
//        if (kakaoAccount != null && kakaoAccount.containsKey("email")) {
//            return (String) kakaoAccount.get("email");
//        } else {
//            throw new RuntimeException("Email not provided by Kakao");
//        }
//    }
//}
