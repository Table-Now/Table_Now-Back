package zerobase.tableNow.domain.user.service;

import org.springframework.http.ResponseEntity;
import zerobase.tableNow.domain.user.dto.*;

import java.util.Map;

public interface UserService {


    //이메일 인증
    boolean emailAuth(String userId, String authKey);

    //로그인
    LocalLoginDto login(LocalLoginDto localLoginDto);

    //회원가입
    RegisterDto register(RegisterDto registerDto);

    //회원 수정
    InfoUpdateDto updateUserInfo(String phone,InfoUpdateDto dto);

   //회원정지
    DeleteDto userDelete(String userId);

    //회원정보 가져오기
    MyInfoDto myInfo(String user);


}
