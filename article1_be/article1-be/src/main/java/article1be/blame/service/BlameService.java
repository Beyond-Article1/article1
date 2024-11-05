package article1be.blame.service;

import article1be.blame.repository.BlameRepository;
import article1be.blame.entity.Blame;
import article1be.board.entity.Board;
import article1be.board.repository.BoardRepository;
import article1be.reply.entity.Reply;
import article1be.reply.repository.ReplyRepository;
import article1be.review.entity.Review;
import article1be.review.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class BlameService {

    private final BlameRepository blameRepository;
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;
    private final ReviewRepository reviewRepository;

    // 게시글 신고
    @Transactional
    public void createBoardBlame(long boardSeq) {

        Blame blame = blameRepository.save(Blame.builder()
                .blameUserSeq(123L)
                .blameBoardSeq(boardSeq)
                .blameReplySeq(null)
                .blameReviewSeq(null)
                .build()
        );

        // 신고된 게시글 찾기
        Optional<Board> blamedBoardList = boardRepository.findById(boardSeq);

        if (blamedBoardList.isPresent()) {
            Board blamedBoard = blamedBoardList.get();

            // 해당 게시글에 대한 신고 개수 확인
            long blameCount = blamedBoardList.stream().toArray().length;

            // 신고 개수가 10개 이상이면 boardIsBlind 값을 true로 업데이트
            if (blameCount >= 10) {
                blamedBoard.setBlind();
                boardRepository.save(blamedBoard); // 변경사항 저장
            }
        }
    }

    // 댓글 신고
    @Transactional
    public void createReplyBlame(long replySeq) {
        Blame blame = blameRepository.save(Blame.builder()
                .blameUserSeq(123L)
                .blameBoardSeq(null)
                .blameReplySeq(replySeq)
                .blameReviewSeq(null)
                .build()
        );

        Optional<Reply> blamedReplyList = replyRepository.findById(replySeq);

        if (blamedReplyList.isPresent()) {
            Reply blamedReply = blamedReplyList.get();

            // 해당 게시글에 대한 신고 개수 확인
            long blameCount = blamedReplyList.stream().toArray().length;

            // 신고 개수가 10개 이상이면 boardIsBlind 값을 true로 업데이트
            if (blameCount >= 10) {
                blamedReply.setBlind();
                replyRepository.save(blamedReply); // 변경사항 저장
            }
        }
    }

    // 리뷰 신고
    @Transactional
    public void createReviewBlame(long reviewSeq) {
        Blame blame = blameRepository.save(Blame.builder()
                .blameUserSeq(123L)
                .blameBoardSeq(null)
                .blameReplySeq(null)
                .blameReviewSeq(reviewSeq)
                .build()
        );

        // 신고된 리뷰 번호로 신고 내역을 가져옴
        Optional<Review> blamedReviewList = reviewRepository.findById(reviewSeq);

        // 존재함
        if (blamedReviewList.isPresent()) {
            Review blamedReview = blamedReviewList.get();
            long blameCount = blamedReviewList.stream().toArray().length;

            // 내역이 10건 이상이면
            if (blameCount >= 10) {
                // 블라인드 처리
                blamedReview.setBlind();
                // 저장
                reviewRepository.save(blamedReview);
            }
        }
    }
}
