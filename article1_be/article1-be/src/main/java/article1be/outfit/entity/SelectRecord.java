package article1be.outfit.entity;
import article1be.outfit.dto.OutfitSelectionRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Table(name = "select_record")
public class SelectRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long selectSeq;

    private Long userSeq;

    private Long situationSeq;

    private LocalDateTime selectDate;

    private LocalDateTime customDate;

    private String customLocation;

    private Integer weatherCode;

    private Double highTemp;

    private Double lowTemp;

    private Double dailyTemp; //일교차

    private Double curTemp;

    private Double precipitation; //강수량

    public static SelectRecord create(OutfitSelectionRequestDTO requestDTO, Long userSeq) {
        SelectRecord selectRecord = new SelectRecord();
        selectRecord.userSeq = userSeq;
        selectRecord.situationSeq = requestDTO.getSituationSeq();
        selectRecord.selectDate = LocalDateTime.now(); // selectDate를 현재 시간으로 설정
        selectRecord.customDate = requestDTO.getCustomDate();
        selectRecord.customLocation = requestDTO.getCustomLocation();
        selectRecord.highTemp = requestDTO.getHighTemp();
        selectRecord.lowTemp = requestDTO.getLowTemp();
        selectRecord.dailyTemp = requestDTO.getDailyTemp();
        selectRecord.curTemp = requestDTO.getCurTemp();
        selectRecord.precipitation = requestDTO.getPrecipitation();
        selectRecord.weatherCode = requestDTO.getWeatherCode();
        return selectRecord;
    }
}
