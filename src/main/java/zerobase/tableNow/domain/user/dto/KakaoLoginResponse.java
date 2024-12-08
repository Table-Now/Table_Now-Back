package zerobase.tableNow.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoLoginResponse {
    private String kakaoAccessToken;
//    private String email;
private String jwtToken;
}