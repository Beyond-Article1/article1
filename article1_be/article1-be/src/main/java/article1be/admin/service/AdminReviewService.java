package article1be.admin.service;

import article1be.admin.dto.AdminReviewDTO;
import article1be.admin.repository.AdminReviewRepository;
import article1be.common.exception.CustomException;
import article1be.common.exception.ErrorCode;
import article1be.review.entity.Review;
import article1be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminReviewService {

    private final AdminReviewRepository reviewRepository;
    private final UserRepository userRepository;

    // 전체 리뷰 목록 조회
    public List<AdminReviewDTO.ReviewInfo> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream()
                .map(review -> {
                    String userNickname = userRepository.findById(review.getUserSeq())
                            .map(user -> user.getUserNickname())
                            .orElse("Unknown User");

                    return new AdminReviewDTO.ReviewInfo(
                            review.getReviewSeq(),
                            review.getUserSeq(),
                            userNickname,
                            review.getReviewLocation(),
                            review.getReviewWeather(),
                            review.getReviewContent(),
                            review.getReviewBlind() ? "BLIND" : "ACTIVE",
                            review.getReviewLikeYn(),
                            review.getReviewBlind(),
                            review.getRegDate()
                    );
                })
                .collect(Collectors.toList());
    }

    // 리뷰 블라인드 상태 업데이트
    public void updateReviewStatus(AdminReviewDTO.ReviewStatusUpdateRequest request) {
        Review review = reviewRepository.findById(request.getReviewSeq())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));

        // 블라인드 상태 업데이트
        review.updateReview(
                review.getReviewContent(),
                review.getReviewWeather(),
                review.getReviewLocation(),
                request.getReviewBlind(),
                review.getReviewLikeYn()
        );

        reviewRepository.save(review);
    }
}
