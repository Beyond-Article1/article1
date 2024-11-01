package article1be.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * == 400  BAD_REQUEST ==
     */
    // 사용자 (User)
    NEED_LOGIN(HttpStatus.BAD_REQUEST, "로그인이 필요한 서비스입니다."),


    /**
     * == 401 UNAUTHORIZED ==
     */
    // 사용자 (user)
    USER_STATUS_BANED(HttpStatus.UNAUTHORIZED, "정지된 회원입니다."),

    /**
     * == 403 FORBIDDEN ==
     */
    // 사용자 (user)
    UNAUTHORIZED_USER(HttpStatus.FORBIDDEN, "자신의 게시물이 아닙니다."),


    /**
     * == 404 NOT_FOUND ==
     */
    // 사용자 (user)
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),


    /**
     * == 409 CONFLICT ==
     */
    // 중복관련
    DUPLICATE_VALUE(HttpStatus.CONFLICT, "이미 존재하는 항목입니다."),


    /**
     * == 500 INTERNAL_SERVER_ERROR ==
     */
    // 서버오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류입니다."),
    INTERNAL_SERVER_IO_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");


    private final HttpStatus status;
    private final String message;
}
