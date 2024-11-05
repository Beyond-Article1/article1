package article1be.blame.entity;

import article1be.common.aggregate.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "BLAME")
@Getter
public class Blame extends BaseTimeEntity {

    @Id
    long blameSeq;                          // 신고 번호
    long blameUserSeq;                      // 신고자
    Long blameBoardSeq;                     // 신고 게시글 번호
    Long blameReplySeq;                     // 신고 댓글 번호
    Long blameReviewSeq;                    // 신고 리뷰 번호
    LocalDateTime blameProcessingDate;      // 신고 처리 날짜시간
    boolean blameStatus;                    // 처리 상태

    @Builder
    public Blame(
            long blameUserSeq,
            Long blameBoardSeq,
            Long blameReplySeq,
            Long blameReviewSeq
    ) {
        this.blameUserSeq = blameUserSeq;
        this.blameBoardSeq = blameBoardSeq;
        this.blameReplySeq = blameReplySeq;
        this.blameReviewSeq = blameReviewSeq;
    }
}
