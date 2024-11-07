package article1be.openweather.dto;

import article1be.openweather.dto.weathers.*;
import lombok.Data;

import java.util.List;

/**
 * 현재 날씨 데이터
 */
@Data
public class OpenWeatherDTO {

    private List<WeatherDTO> weather;

    // 내부 매개변수
    private String base;

    private MainDTO main;

    private CloudsDTO clouds;

    private RainDTO rain;

    private SnowDTO snow;

    /** 가시성 */
    private int visibility;

    /** 데이터 계산 시간, 유닉스, UTC */
    private long dt;

    /** UTC에서 초 단위로 이동 */
    private int timezone;

    /** 도시 ID */
    private long id;

    /** 도시 이름 */
    private String name;

    /** 내부 매개 변수 */
    private int cod;

    @Data
    public static class Sys {

        private int type;

        private int id;

        /** 국가 코드 (GB, JP 등) */
        private String country;

        /** 일출 시간, 유닉스, UTC */
        private long sunrise;

        /** 일몰 시간, 유닉스, UTC */
        private long sunset;
    }
}