package article1be.user.service;

import article1be.user.userInfo.GoogleUserInfo;
import article1be.user.userInfo.NaverUserInfo;
import article1be.user.userInfo.KakaoUserInfo;
import article1be.user.userInfo.OAuth2UserInfo;
import article1be.config.PrincipalDetails;
import article1be.user.entity.*;
import article1be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SocialLoginService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    // 구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
    // 함수 종료 시, @AuthenticationPrincipal 어노테이션 생성
    @Override // 후처리 기능
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // getClientRegistration -> registrationId로 어떤 OAuth로 로그인 했는지 확인 가능
        System.out.println("getClientRegistration = " + userRequest.getClientRegistration()); // 서버의 기본 정보를 출력
        System.out.println("getAccessToken = " + userRequest.getAccessToken().getTokenValue()); // 토큰 값
        // 구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인 완료-> code 리턴 (OAuth-Client 라이브러리) -> AccessToken 요청
        // 위까지가 userRequest 정보

        // 소셜 로그인 provider (카카오, 네이버, 구글)로부터 사용자 정보를 가져오는 부분
        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println("getAttributes = " + oAuth2User.getAttributes()); // 회원 정보 출력

        // 회원 가입을 강제로 진행해 볼 예정
        OAuth2UserInfo oAuth2Userinfo = null;

        if(userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            System.out.println("카카오 로그인 요청");

            oAuth2Userinfo  = new KakaoUserInfo(oAuth2User.getAttributes()); // KakaoUserInfo에 요청
        } else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            System.out.println("네이버 로그인 요청");

            oAuth2Userinfo  = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response")); // NaverUserInfo에 요청
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("구글 로그인 요청");

            oAuth2Userinfo = new GoogleUserInfo(oAuth2User.getAttributes()); // GoogleUserInfo에 요청
        } else System.out.println("우리는 카카오, 네이버, 구글만 지원해요.");

        String userSocialSite = oAuth2Userinfo.getGetProvider().toUpperCase(); // kakao, naver, google
        String userId = oAuth2Userinfo.getEmail();
        String userName = oAuth2Userinfo.getName();

        String userPhoneNum = "";

        if(userSocialSite.equals("KAKAO")) {
            String rawPhoneNumber = oAuth2Userinfo.getPhoneNumber();

            if (rawPhoneNumber != null && rawPhoneNumber.startsWith("+82")) userPhoneNum = "010" + rawPhoneNumber.substring(6); // +82를 제거하고 010으로 시작하게 변환
        } else if(userSocialSite.equals("NAVER")) {
            userPhoneNum = oAuth2Userinfo.getMobile();
        } else userPhoneNum = "회원 휴대 전화 번호";

        String birthYear = "";
        String birthDay = "";
        String birthDate = "";
        LocalDate userBirthDate;

        if(userSocialSite.equals("KAKAO")) {
            birthYear = oAuth2Userinfo.getBirthyear();
            birthDay = oAuth2Userinfo.getBirthday(); // MMDD 형식

            int month = Integer.parseInt(birthDay.substring(0, 2)); // MM
            int day = Integer.parseInt(birthDay.substring(2, 4));   // DD

            userBirthDate = LocalDate.of(Integer.parseInt(birthYear), month, day);
        } else if(userSocialSite.equals("NAVER")) {
            birthYear = oAuth2Userinfo.getBirthyear();
            birthDay = oAuth2Userinfo.getBirthday();
            birthDate = birthYear + "-" + birthDay;
            userBirthDate = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } else userBirthDate = LocalDate.parse("1234-12-31");

        String userGender = "";

        if(userSocialSite.equals("KAKAO")) {
            userGender = oAuth2Userinfo.getGender().toUpperCase();
        } else if(userSocialSite.equals("NAVER")) {
            if (oAuth2Userinfo.getGender().charAt(0) == 'M') {
                userGender = "MALE";
            } else userGender = "FEMALE";
        } else userGender = "MALE";

        String userState = "ACTIVE";
        String userAuth = "USER";

        // 회원 가입 여부 확인
        User user = userRepository.findByUserId(userId);

        if(user == null) {
            System.out.println("OAuth 로그인이 최초입니다.");

            user = User.builder()
                    .userSocialSite(UserSocialSite.valueOf(userSocialSite))
                    .userId(userId)
                    .userName(userName)
                    .userPhoneNum(userPhoneNum)
                    .userBirthDate(userBirthDate)
                    .userGender(UserGender.valueOf(userGender))
                    .userState(UserState.valueOf(userState))
                    .userAuth(UserAuth.valueOf(userAuth))
                    .build();

            userRepository.save(user);

        } else System.out.println("로그인을 이미 한 적이 있습니다. 당신은 회원 가입이 되어 있습니다.");

        return new PrincipalDetails(user, oAuth2User.getAttributes()); // 생성되면 Authentication 객체 안으로 삽입
    }

}
