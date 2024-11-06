package article1be.board.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BoardDTO {

    private long boardSeq;                     // 게시물 번호

    private long userSeq;                       // 게시물 작성자
    private String boardTitle;                  // 게시물 제목
    private String boardContent;                // 게시물 내용
    private List<PictureDTO> boardPictureList;  // 게시물 사진
    private LocalDateTime regDate;              // 게시물 등록일
    private LocalDateTime upDate;               // 게시물 수정일
    private LocalDateTime delDate;              // 게시물 삭제일
    private boolean boardIsBlind;                    // 게시물 블라인드 여부
    private boolean boardIsNotice;                   // 게시물 공지 여부
}
