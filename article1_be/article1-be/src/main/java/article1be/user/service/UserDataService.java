// UserDataService.java
package article1be.user.service;

import article1be.common.exception.CustomException;
import article1be.common.exception.ErrorCode;
import article1be.user.dto.UserDataDTO;
import article1be.user.entity.User;
import article1be.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDataService {

    private final UserRepository userRepository;

    // 유저 스타일 및 컨디션 조회
    @Transactional
    public UserDataDTO getUserStyle(Long userSeq) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        UserDataDTO userData = new UserDataDTO();
        userData.setUserSeq(user.getUserSeq());
        userData.setUserNickname(user.getUserNickname());
        userData.setStyleSeq(user.getStyleSeq());
        userData.setConditionSeq(user.getConditionSeq());

        return userData;
    }

    // 사용자 스타일 및 체질 수정
    @Transactional
    public UserDataDTO updateUserStyle(Long userSeq, UserDataDTO userDataDTO) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        user.setUserNickname(userDataDTO.getUserNickname());
        user.setStyleSeq(userDataDTO.getStyleSeq());
        user.setConditionSeq(userDataDTO.getConditionSeq());

        userRepository.save(user);

        UserDataDTO updatedUserData = new UserDataDTO();
        updatedUserData.setUserSeq(user.getUserSeq());
        updatedUserData.setUserNickname(user.getUserNickname());
        updatedUserData.setStyleSeq(user.getStyleSeq());
        updatedUserData.setConditionSeq(user.getConditionSeq());

        return updatedUserData;
    }
}
