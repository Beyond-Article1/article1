package article1be.userpick.service;

import article1be.common.exception.CustomException;
import article1be.common.exception.ErrorCode;
import article1be.userpick.dto.UserPickDTO;
import article1be.userpick.entity.UserPick;
import article1be.userpick.repository.UserPickRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserPickService {

    private final UserPickRepository userPickRepository;

    // 유저 스타일 및 컨디션 조회
    @Transactional
    public UserPickDTO getUserStyle(Long userSeq) {
        UserPick userPick = userPickRepository.findById(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        UserPickDTO userPickDTO = new UserPickDTO();
        userPickDTO.setUserSeq(userPick.getUserSeq());
        userPickDTO.setStyleSeq(userPick.getStyleSeq());
        userPickDTO.setConditionSeq(userPick.getConditionSeq());

        return userPickDTO;
    }

    // 사용자 스타일 및 체질 수정
    @Transactional
    public UserPickDTO updateUserStyle(Long userSeq, UserPickDTO userPickDTO) {
        UserPick userPick = userPickRepository.findById(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        userPick.setStyleSeq(userPickDTO.getStyleSeq());
        userPick.setConditionSeq(userPickDTO.getConditionSeq());

        userPickRepository.save(userPick);

        UserPickDTO updatedUserPickDTO = new UserPickDTO();
        updatedUserPickDTO.setUserSeq(userPick.getUserSeq());
        updatedUserPickDTO.setStyleSeq(userPick.getStyleSeq());
        updatedUserPickDTO.setConditionSeq(userPick.getConditionSeq());

        return updatedUserPickDTO;
    }
}
