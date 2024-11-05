// UserDataController.java
package article1be.user.controller;

import article1be.user.dto.UserDataDTO;
import article1be.user.service.UserDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserDataController {

    private final UserDataService userDataService;

    // 사용자 스타일 및 체질 조회
    @GetMapping("/{userSeq}/style")
    public UserDataDTO getUserStyle(@PathVariable Long userSeq) {
        return userDataService.getUserStyle(userSeq);
    }

    // 사용자 스타일 및 체질 수정
    @PutMapping("/{userSeq}/style")
    public UserDataDTO updateUserStyle(@PathVariable Long userSeq, @RequestBody UserDataDTO userDataDTO) {
        return userDataService.updateUserStyle(userSeq, userDataDTO);
    }
}
