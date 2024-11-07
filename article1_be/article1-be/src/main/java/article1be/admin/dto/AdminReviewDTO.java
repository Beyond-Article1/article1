package article1be.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AdminReviewDTO {

    // 전체 리뷰 목록 조회용 DTO
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReviewInfo {
        private Long reviewSeq;         // 리뷰 번호
        private Long userSeq;           // 유저 번호
        private String userNickname;    // 유저 닉네임
        private double location;        // 위치 정보
        private double weather;         // 날씨 정보
        private String reviewContent;   // 리뷰 내용
        private String reviewStatus;    // 리뷰 상태 (ACTIVE 또는 BLIND)
        private boolean reviewLikeYn;
        private boolean reviewBlind;
        private LocalDateTime regDate;
    }

    // 리뷰 상태 업데이트 요청 DTO
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReviewStatusUpdateRequest {
        private Long reviewSeq;         // 리뷰 번호
        private Boolean reviewBlind = false;    // 블라인드 여부
    }
}
