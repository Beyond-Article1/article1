package article1be.openweather.dto;

import article1be.openweather.dto.airs.ComponentsDTO;
import article1be.openweather.dto.airs.MainAirDTO;
import article1be.openweather.dto.weathers.CoordDTO;
import lombok.Data;

import java.util.List;

@Data
public class OpenWeatherAirDTO {

    private CoordDTO coord;
    private List<AirList> list; // AirList는 여러 개일 수 있음.

    @Data
    public static class AirList {
        private int dt; // 시간
        private MainAirDTO main; // 주요 정보
        private ComponentsDTO components; // 성분 정보
    }

//    @Data
//    public static class Main {
//        private int aqi; // 공기질 지수
//    }

//    @Data
//    public static class Components {
//        private double co; // 일산화탄소
//        private double no; // 질소
//        private double no2; // 이산화질소
//        private double o3; // 오존
//        private double so2; // 이산화황
//        private double pm2_5; // 미세먼지(PM2.5)
//        private double pm10; // 미세먼지(PM10)
//        private double nh3; // 암모니아
//    }
}