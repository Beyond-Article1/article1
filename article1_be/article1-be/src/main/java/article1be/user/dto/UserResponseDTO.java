package article1be.user.dto;

import article1be.user.entity.UserGender;
import article1be.user.entity.UserSocialSite;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long userSeq;                   // 회원 번호
    private UserSocialSite userSocialSite;  // 소셜 로그인 사이트
    private String userId;                  // 계정 (이메일)
    private String userName;                // 이름
    private String userNickname;            // 닉네임
    private String userPhoneNum;            // 휴대 전화 번호
    private LocalDate userBirthDate;        // 생년월일
    private UserGender userGender;          // 성별
    private LocalDateTime regDate;          // 가입 일자

}
