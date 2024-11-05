package article1be.review.entity;

import article1be.common.aggregate.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "Review")
@Getter
@SQLDelete(sql = "UPDATE Review SET del_date = LOCALTIME WHERE review_seq = ?")
public class Review extends BaseTimeEntity {

    @Id
    @Column(name = "review_seq", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewSeq;         // 리뷰 번호

    @Column(name = "user_seq", nullable = false)
    private Long userSeq;           // 유저 번호

    @Column(name = "select_seq", nullable = false)
    private Long selectSeq;         // 선택 번호

    @Column(name = "review_content", nullable = false, length = 255)
    private String reviewContent;   // 리뷰 내용

    @Column(name = "review_weather", nullable = false)
    private Double reviewWeather;  // 날씨 정보

    @Column(name = "review_location", nullable = false)
    private Double reviewLocation; // 위치 정보

    @Column(name = "review_blind", nullable = false)
    private Boolean reviewBlind;  // 블라인드 여부

    @Column(name = "review_like_yn", nullable = false)
    private Boolean reviewLikeYn; // 좋아요 여부


    public Review(Long userSeq, Long selectSeq, String reviewContent, Double reviewWeather,
                  Double reviewLocation, Boolean reviewBlind, Boolean reviewLikeYn) {
        this.userSeq = userSeq;
        this.selectSeq = selectSeq;
        this.reviewContent = reviewContent;
        this.reviewWeather = reviewWeather;
        this.reviewLocation = reviewLocation;
        this.reviewBlind = reviewBlind;
        this.reviewLikeYn = reviewLikeYn;
    }


    // 리뷰 내용 업데이트
    public void updateReview(String reviewContent, Double reviewWeather, Double reviewLocation,
                             Boolean reviewBlind, Boolean reviewLikeYn) {
        this.reviewContent = reviewContent;
        this.reviewWeather = reviewWeather;
        this.reviewLocation = reviewLocation;
        this.reviewBlind = reviewBlind;
        this.reviewLikeYn = reviewLikeYn;
    }

    // 블라인드 처리
    public void setBlind() {
        this.reviewBlind = true;
    }
}
