package article1be.board.entity;

import article1be.board.dto.RequestBoard;
import article1be.common.aggregate.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "BOARD")
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long boardSeq;                          // 게시글 번호

    Long userSeq;                           // 작성자 번호
    @Column(length = 50)
    String boardTitle;                      // 게시글 제목
    @Column(length = 1000)
    String boardContent;                    // 게시글 내용
    // 작성일은 BaseTimeEntity 에서 상속        // 작성일
    // 수정일도 BaseTimeEntity 에서 상속        // 수정일
    LocalDateTime delDate;                  // 삭제일
    Boolean boardIsBlind;                        // 블라인드 여부
    Boolean boardIsNotice;                       // 공지 여부

    public Board create(
            long userSeq,
            String boardTitle,
            String boardContent
    ) {
        Board newBoard = new Board();

        newBoard.boardSeq = null;
        newBoard.userSeq = userSeq;
        newBoard.boardTitle = boardTitle;
        newBoard.boardContent = boardContent;
        newBoard.delDate = null;
        newBoard.boardIsBlind = false;
        newBoard.boardIsNotice = false;

        return newBoard;
    }

    public void update(RequestBoard requestBoard) {
        this.boardTitle = requestBoard.getBoardTitle();
        this.boardContent = requestBoard.getBoardContent();
    }

    public void setBlind() {
        this.boardIsBlind = true;
    }
}
