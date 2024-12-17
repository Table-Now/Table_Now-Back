package zerobase.tableNow.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"내부 서버 오류가 발생했습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST,"요청 데이터가 유효하지 않습니다."),
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"사용자를 찾을 수 없습니다."),
    USER_NOT_AUTHENTICATED(HttpStatus.FORBIDDEN, "사용자가 인증되지 않았습니다. 로그인 후 다시 시도하세요"),
    USER_CANNOT_REGISTER_PRODUCT(HttpStatus.FORBIDDEN, "일반 사용자는 매장을 등록할 수 없습니다."),
    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀리뷰의 비밀번호는 4자리 숫자로 설정해야 합니다."),
    USER_STATUS_STOP(HttpStatus.FORBIDDEN, "해당 아이디가 탈퇴한 회원이거나 정지된 회원입니다."),
    EMAIL_NOT_AUTHENTICATED(HttpStatus.FORBIDDEN, "가입하신 이메일로 인증을 완료해주세요."),
    DELETE_USER(HttpStatus.BAD_REQUEST, "탈퇴한 계정입니다."),

    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 상점을 찾을 수 없습니다."),
    MENUS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 메뉴를 찾을 수 없습니다."),
    PRODUCT_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "동일한 상점이 이미 존재합니다."),

    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 예약을 찾을 수 없습니다."),
    STORE_RESTRICTED(HttpStatus.FORBIDDEN, "해당 매장은 현재 줄서기 금지 상태입니다."),
    CONFLICT(HttpStatus.CONFLICT, "이미 다른 매장에 줄서기를 등록하셨습니다. 기존 예약을 취소하거나 확인해주세요."),

    PRODUCT_NOT_PURCHASED(HttpStatus.FORBIDDEN, "해당 매장을 이용한 적이 없으므로 리뷰를 남길 수 없습니다."),
    DUPLICATE_REVIEW(HttpStatus.FORBIDDEN, "이미 해당 매장에 대해 리뷰를 남겼습니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 리뷰가 없습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "리뷰를 수정할 권한이 없습니다."),
    EMAIL_SEND_FAILED(HttpStatus.FORBIDDEN,"내정보 수정 -> 인증 메일 발송 실패");



    private final HttpStatus status;
    private final String description;

}
