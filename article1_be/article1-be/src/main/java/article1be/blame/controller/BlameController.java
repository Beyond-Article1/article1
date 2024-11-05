package article1be.blame.controller;

import article1be.blame.service.BlameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "신고",
        description = "신고 관련 API"
)
@RestController
@AllArgsConstructor
@RequestMapping(value = "/report")
public class BlameController {

    private final BlameService service;

    // 게시글 신고
    @Operation(
            summary = "게시글 신고",
            description = "게시글 번호를 입력받아 해당 게시글을 신고"
    )
    @PostMapping(value = "/{boardSeq}")
    public ResponseEntity<String> blameBoard(@PathVariable("boardSeq") long boardSeq) {
        service.createBoardBlame(boardSeq);

        return ResponseEntity.ok("신고가 완료되었습니다.");
    }

    // 댓글 신고
    @Operation(
            summary = "댓글 신고",
            description = "댓글 번호를 입력받아 해당 댓글을 신고"
    )
    @PostMapping(value = "/{replySeq}")
    public ResponseEntity<String> blameReply(@PathVariable("replySeq") long replySeq) {
        service.createReplyBlame(replySeq);

        return ResponseEntity.ok("신고가 완료되었습니다.");
    }

    // 리뷰 신고
    @Operation(
            summary = "리뷰 신고",
            description = "리뷰 번호를 입력받아 해당 리뷰를 신고"
    )
    @PostMapping(value = "/{reviewSeq}")
    public ResponseEntity<String> blameReview(@PathVariable("reviewSeq") long reviewSeq) {
        service.createReviewBlame(reviewSeq);

        return ResponseEntity.ok("신고가 완료되었습니다.");
    }
}
