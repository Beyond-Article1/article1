package article1be.outfit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutfitSelectionRequestDTO {
    private Long situationSeq;
    private LocalDateTime customDate;
    private String customLocation;
    private Integer weatherCode;
    private Double highTemp;
    private Double lowTemp;
    private Double dailyTemp;
    private Double curTemp;
    private Double precipitation; //강수량
    private Long topSeq;
    private Long bottomSeq;     // 하의 ID
    private Long shoesSeq;      // 신발 ID
    private Long outerSeq;      // 아우터 ID (선택)
    private List<Long> accessorySeq; // 악세서리 ID들 (선택)
}
