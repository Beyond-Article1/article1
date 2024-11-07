package article1be.reply.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReplyDTO {

    private long replySeq;
    private long boardSeq;
    private long replyUserSeq;
    private String replyContent;
    private LocalDateTime regDate;
    private LocalDateTime delDate;
    private boolean replyIsBlind;

    @Builder
    public ReplyDTO(
            Long replySeq,
            long boardSeq,
            long replyUserSeq,
            String replyContent,
            LocalDateTime regDate,
            LocalDateTime delDate,
            boolean replyIsBlind
    ) {
        this.replySeq = replySeq;
        this.boardSeq = boardSeq;
        this.replyUserSeq = replyUserSeq;
        this.replyContent = replyContent;
        this.regDate = regDate;
        this.delDate = delDate;
        this.replyIsBlind = replyIsBlind;
    }
}
