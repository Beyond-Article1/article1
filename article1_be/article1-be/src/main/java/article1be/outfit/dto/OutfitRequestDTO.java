package article1be.outfit.dto;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutfitRequestDTO {
    private Long situationSeq;
    private LocalDateTime requestedAt;   // 추천받을 날짜와 시간
    private double latitude; // 위도
    private double longitude; //경도
}
