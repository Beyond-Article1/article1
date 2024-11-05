package article1be.review.controller;

import article1be.review.dto.ReviewDTO;
import article1be.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "리뷰",
        description = "리뷰 관련 API"
)
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 모든 리뷰 조회
    @Operation(
            summary = "모든 리뷰 조회",
            description = "리뷰 번호, 작성자, 닉네임, 위치 정보, 날씨 정보, 리뷰 내용, 리뷰 상태를 목록으로 반환"
    )
    @GetMapping
    public List<ReviewDTO> getAllReviews() {
        return reviewService.getAllReviews();
    }

    // 특정 사용자의 리뷰 조회
    @Operation(
            summary = "특정 사용자의 리뷰 조회",
            description = "사용자 번호를 입력받아 해당 사용자의 리뷰(리뷰 번호, 작성자, 닉네임, 위치 정보, 날씨 정보, 리뷰 내용, 리뷰 상태) 목록을 반환"
    )
    @GetMapping("/user/{userSeq}")
    public List<ReviewDTO> getReviewsByUser(@PathVariable Long userSeq) {
        return reviewService.getReviewsByUser(userSeq);
    }

    // 리뷰 등록
    @Operation(
            summary = "리뷰 등록",
            description = "리뷰 번호, 작성자 번호, 사용자 닉네임, 위치 정보, 날씨 정보, 리뷰 내용, 리뷰 상태를 입력받아 새로운 리뷰 등록"
    )
    @PostMapping("/user/{userSeq}")
    public ReviewDTO createReview(@PathVariable Long userSeq, @RequestBody ReviewDTO reviewDto) {
        reviewDto.setUserSeq(userSeq);
        return reviewService.createReview(reviewDto);
    }

    // 리뷰 수정
    @Operation(
            summary = "리뷰 수정",
            description = "리뷰 번호, 사용자 번호, 사용자 닉네임, 위치 정보, 날씨 정보, 리뷰 내용, 리뷰 상태를 입력받아 수정(작성자 또는 관리자만 가능)"
    )
    @PutMapping("/{reviewSeq}")
    public ReviewDTO updateReview(@PathVariable Long reviewSeq, @RequestBody ReviewDTO reviewDto) {
        return reviewService.updateReview(reviewSeq, reviewDto);
    }

    // 리뷰 삭제
    @Operation(
            summary = "리뷰 삭제",
            description = "리뷰 번호를 받아 해당 리뷰 삭제(작성자 또는 관리자만 가능)"
    )
    @DeleteMapping("/{reviewSeq}")
    public void deleteReview(@PathVariable Long reviewSeq) {
        reviewService.deleteReview(reviewSeq);
    }

}
