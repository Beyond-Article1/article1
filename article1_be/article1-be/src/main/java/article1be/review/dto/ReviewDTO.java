package article1be.review.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private Long reviewSeq;         // 리뷰 번호
    private Long userSeq;           // 유저 번호
    private String userNickname;    // 유저 닉네임
    private Double location;        // 위치 정보 (Double로 변경)
    private Double weather;         // 날씨 정보 (Double로 변경)
    private String reviewContent;   // 리뷰 내용
    private String reviewStatus;    // 리뷰 상태 (ACTIVE 또는 BLIND)
}
