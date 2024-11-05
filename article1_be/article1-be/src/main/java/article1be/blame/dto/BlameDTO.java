package article1be.blame.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlameDTO {

    long blameUserSeq;                      // 신고자

    long blameBoardSeq;                     // 신고 게시글 번호
    long blameReplySeq;                     // 신고 댓글 번호
    long blameReviewSeq;                    // 신고 리뷰 번호
    LocalDateTime blameProcessingDate;      // 신고 처리 날짜시간
    boolean blameStatus;                    // 처리 상태
}
