package article1be.admin.service;

import article1be.admin.dto.AdminDTO;
import article1be.admin.repository.AdminRepository;
import article1be.common.exception.CustomException;
import article1be.common.exception.ErrorCode;
import article1be.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    // 전체 회원 정보 조회
    public List<AdminDTO.UserInfo> getAllMembers() {
        List<User> users = adminRepository.findAll();
        if (users.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }
        return users.stream()
                .map(user -> new AdminDTO.UserInfo(
                        user.getUserSeq(),
                        user.getUserId(),
                        user.getUserName(),
                        user.getUserNickname(),
                        user.getUserPhoneNum(),
                        user.getUserBirthDate(),
                        user.getUserGender(),
                        user.getUserState()))
                .collect(Collectors.toList());
    }

    // 회원 정보 상세 조회
    public AdminDTO.UserInfo getMemberDetail(Long userSeq) {
        User user = adminRepository.findById(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        return new AdminDTO.UserInfo(
                user.getUserSeq(),
                user.getUserId(),
                user.getUserName(),
                user.getUserNickname(),
                user.getUserPhoneNum(),
                user.getUserBirthDate(),
                user.getUserGender(),
                user.getUserState()
        );
    }

    // 회원 상태, 닉네임, 권환 변경
    public void updateUser(AdminDTO.UserUpdateRequest updateRequest) {
        User user = adminRepository.findById(updateRequest.getUserSeq())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        user.AdminUserInfo(
                updateRequest.getUserNickname(),
                updateRequest.getUserState(),
                updateRequest.getUserAuth());

        adminRepository.save(user);
    }
}
