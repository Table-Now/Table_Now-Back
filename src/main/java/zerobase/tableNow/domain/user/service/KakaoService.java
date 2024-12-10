package zerobase.tableNow.domain.user.service;

import org.springframework.http.ResponseEntity;
import zerobase.tableNow.domain.user.dto.DeleteDto;
import zerobase.tableNow.domain.user.dto.InfoUpdateDto;
import zerobase.tableNow.domain.user.dto.KakaoLoginResponse;
import zerobase.tableNow.domain.user.dto.MyInfoDto;

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

    /**
     * 회원 수정
     * @param dto phone
     * @return 회원수정
     */
    InfoUpdateDto infoUpdate(InfoUpdateDto dto);

    /**
     * 회원 정지
     * @param userId user
     * @return user
     */
    DeleteDto userDelete(String userId);

    /**
     * 회원 정보 가져오기
     * @param user user
     * @return user
     */
    MyInfoDto myInfo(String user);


}
