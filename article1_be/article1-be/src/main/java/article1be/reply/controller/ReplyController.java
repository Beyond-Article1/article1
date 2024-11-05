package article1be.reply.controller;

import article1be.reply.dto.ReplyDTO;
import article1be.reply.dto.RequestReply;
import article1be.reply.service.ReplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(
        name = "댓글",
        description = "댓글 관련 API"
)
@RestController
@RequestMapping(value = "/reply")
public class ReplyController {

    private final ReplyService service;

    @Autowired
    public ReplyController(ReplyService service) {
        this.service = service;
    }

    // 댓글 목록 조회
    @Operation(
            summary = "댓글 목록 조회",
            description = "댓글 번호, 게시판 번호, 작성자 번호, 댓글 내용, 등록일, 삭제일, 블라인드 여부를 목록으로 반환"
    )
    @GetMapping(value = "/{boardSeq}")
    public ResponseEntity<List<ReplyDTO>> getReply(@PathVariable Long boardSeq) {

        // 조회결과 없음
        if (service.getReplyList(boardSeq) == null || service.getReplyList(boardSeq).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // 조회 성공
        else {
            return ResponseEntity.ok(service.getReplyList(boardSeq));
        }
    }

    // 댓글 작성
    @Operation(
            summary = "댓글 등록",
            description = "게시글 번호와 댓글 내용을 받아 새로운 댓글을 등록"
    )
    @PostMapping(value = {"/{boardSeq}"})
    public ResponseEntity<String> createReply(
            @PathVariable Long boardSeq,
            @RequestBody RequestReply requestReply
    ) {

        // 성공
        if (service.createReply(boardSeq, requestReply) != null) {
            return ResponseEntity.ok("댓글 작성이 완료되었습니다.");
        }
        // 실패
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    // 댓글 삭제
    @Operation(
            summary = "댓글 삭제",
            description = "댓글 번호를 입력받아 해당 댓글을 삭제(작성자 또는 관리자만 가능)"
    )
    @DeleteMapping(value = "/{replySeq}")
    public ResponseEntity<String> deleteReply(@PathVariable Long replySeq) {
        // 블라인드 처리 성공
        if (service.deleteReply(replySeq)) {
            return ResponseEntity.ok("삭제에 성공하였습니다."); // 성공 메시지
        }
        // 블라인드 처리 실패
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
