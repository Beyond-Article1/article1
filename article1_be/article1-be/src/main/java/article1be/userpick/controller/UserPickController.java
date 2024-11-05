package article1be.userpick.controller;

import article1be.userpick.dto.UserPickDTO;
import article1be.userpick.service.UserPickService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/userpick")
@RequiredArgsConstructor
public class UserPickController {

    private final UserPickService userPickService;

    // 사용자 스타일 및 체질 조회
    @GetMapping("/{userSeq}/userpick")
    public UserPickDTO getUserStyle(@PathVariable Long userSeq) {
        return userPickService.getUserStyle(userSeq);
    }

    // 사용자 스타일 및 체질 수정
    @PutMapping("/{userSeq}/userpick")
    public UserPickDTO updateUserStyle(@PathVariable Long userSeq, @RequestBody UserPickDTO userPickDTO) {
        return userPickService.updateUserStyle(userSeq, userPickDTO);
    }
}
