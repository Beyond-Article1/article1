package article1be.user.service;

import article1be.common.exception.CustomException;
import article1be.common.exception.ErrorCode;
import article1be.outfit.entity.Style;
import article1be.security.util.SecurityUtil;
import article1be.user.dto.PreferenceResponseDTO;
import article1be.user.dto.UserDataDTO;
import article1be.user.dto.UserPreferDTO;
import article1be.user.dto.UserResponseDTO;
import article1be.user.entity.Condition;
import article1be.user.entity.User;
import article1be.user.repository.ConditionRepository;
import article1be.user.repository.StyleRepository;
import article1be.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final StyleRepository styleRepository;
    private final ConditionRepository conditionRepository;

    @Override
    public UserDetails loadUserByUsername(String userSeq) throws UsernameNotFoundException {
        // 인증 토큰에 담긴 userSeq가 메소드로 넘어오므로 해당 값을 기준으로 DB에서 조회 (전달된 아이디 기준으로 DB 조회)
        User loginUser = userRepository.findByUserSeq(Long.parseLong(userSeq))  // JPA 쓰고 있으므로 findByUserId 활용
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userSeq : " + userSeq));  // Id가 존재하지 않으면 (해당 Id가 조회되지 않으면) Exception 발생

        // 권한이 여러 가지 있을 수 있으므로, 기본적으로 권한은 Collection Type으로 담도록 구성
        // 우리는 심플하게 두 가지 (USER, ADMIN)으로만 나누었기 때문에, 코드로 가공해서 기재되어 있는 USER or ADMIN 권한 하나만 주입
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(loginUser.getUserAuth().name()));

        // 소셜 로그인에서는 비밀번호를 사용하지 않으므로 빈 문자열로 처리
        String password = "";

        // Spring Security에서 제공하는 User 객체 반환
        return new org.springframework.security.core.userdetails.User(
                String.valueOf(loginUser.getUserSeq()), password, grantedAuthorities);
    }

    // 회원 닉네임, 선호도 등록
    @Transactional
    public void createUserData(@Valid UserDataDTO userData) {
        // 닉네임 중복 검증
        if (checkUserNickname(userData.getUserNickname(), null)) {
            Long userSeq = SecurityUtil.getCurrentUserSeq();

            User findUser = userRepository.findByUserSeq(userSeq)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

            // styleSeq와 conditionSeq에 따라 해당 Style과 Condition을 찾는다
            Style style = styleRepository.findStyleByStyleSeq(userData.getStyleSeq());
            Condition condition = conditionRepository.findConditionByConditionSeq(userData.getConditionSeq());

            // User 엔티티에 style, condition 설정
            findUser.createUserData(userData, style, condition);

        }
    }

    // 회원 개인 정보 조회
    @Transactional
    public UserResponseDTO getUserDetail(Long userSeq) {
        User findUser = userRepository.findByUserSeq(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        return new UserResponseDTO(
                findUser.getUserSeq(),
                findUser.getUserSocialSite(),
                findUser.getUserId(),
                findUser.getUserName(),
                findUser.getUserNickname(),
                findUser.getUserPhoneNum(),
                findUser.getUserBirthDate(),
                findUser.getUserGender(),
                findUser.getRegDate()
        );
    }

    // 회원 정보 (닉네임) 수정
    @Transactional
    public void updateUser(Long userSeq, String newNickname) {
        // 닉네임 중복 검증
        if (checkUserNickname(newNickname, userSeq)) {
            User findUser = userRepository.findByUserSeq(userSeq)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

            findUser.updateUser(newNickname);

            userRepository.save(findUser);
        }
    }

    // 회원 탈퇴 (soft delete)
    @Transactional
    public void deleteUser(Long userSeq) {
        userRepository.deleteById(userSeq);
    }

    /* 선호도 조회 */
    @Transactional
    public PreferenceResponseDTO getUserPreference(Long userSeq) {

        // User 엔티티와 관련된 Style, Condition을 한 번에 조회
        User user = userRepository.findByUserSeqWithStyleAndCondition(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // PreferenceResponseDTO에 User의 스타일과 체질 정보를 담아서 반환
        return new PreferenceResponseDTO(
                user.getStyle() != null ? user.getStyle().getStyleName() : null,
                user.getCondition() != null ? user.getCondition().getConditionName() : null
        );

    }

    /* 선호도 수정 */
    @Transactional
    public void updatePreference(Long userSeq, UserPreferDTO preferData) {

        User findUser = userRepository.findByUserSeq(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // styleSeq와 conditionSeq에 따라 해당 Style과 Condition을 찾는다
        Style style = styleRepository.findStyleByStyleSeq(preferData.getStyleSeq());
        Condition condition = conditionRepository.findConditionByConditionSeq(preferData.getConditionSeq());

        // User 엔티티에 style, condition 설정
        findUser.updateUserPrefer(style, condition);

    }

    // 닉네임 중복 검증
    @Transactional
    public boolean checkUserNickname(String userNickname, Long userSeq) {
        Optional<User> findNickname = userRepository.findByUserNickname(userNickname);

        if(findNickname.isEmpty()) return true;
        if(Objects.equals(findNickname.get().getUserSeq(), userSeq)) return true;

        log.info("닉네임 값 중복 {}", userNickname);

        throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
    }

}
