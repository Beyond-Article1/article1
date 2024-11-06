package article1be.user.userInfo;

public interface OAuth2UserInfo {

    String getProviderId(); // kakao, naver, google
    String getGetProvider(); // kakao, naver, google
    String getEmail(); // kakao, naver, google
    String getName(); // kakao, naver, google
    String getGender(); // kakao, naver
    String getBirthday(); // kakao, naver
    String getBirthyear(); // kakao, naver
    String getMobile(); // naver
    String getPhoneNumber(); // kakao

}
