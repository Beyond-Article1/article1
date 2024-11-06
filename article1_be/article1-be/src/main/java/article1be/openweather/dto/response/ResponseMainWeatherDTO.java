package article1be.openweather.dto.response;

import article1be.openweather.dto.weathers.WeatherListDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ResponseMainWeatherDTO {

    // 지정시간 날씨코드
    private int nowWeatherCode;

    // 지정시간 날씨아이콘
    private String nowWeatherIcon;

    // 지정시간 설명
    private String nowWeatherDescription;

    // 지정시간 온도
    private float nowTemp;

    // 지정시간 체감 온도
    private float nowFeelsLike;

    // 하루 중 최저기온
    private float lowTemp;

    // 하루 중 최고기온
    private float highTemp;

    // 지정시간 미세먼지(PM2.5)
    private double pm2_5;

    // 지정시간 초미세먼지(PM10)
    private double pm10;

    // 지정시점 ~ 24시이전까지의 날씨 데이터
    private List<ChangeWeatherListDTO> list;

    // 현재시간
    private LocalDateTime nowTime;

    // WeatherListDTO 변환 및 세팅
    public void setList(List<WeatherListDTO> list) {
        // 변환한 데이터를 담을 리스트 생성
        List<ChangeWeatherListDTO> responseList = new ArrayList<>();

        // 데이터 변환
        for (WeatherListDTO weatherListDTO : list) {

            // DTO 데이터 변환
            ChangeWeatherListDTO changeWeatherListDTO = new ChangeWeatherListDTO();
            changeWeatherListDTO.setDt(weatherListDTO.getDt());
            changeWeatherListDTO.setWeather(weatherListDTO.getWeather());

            // MainDTO 데이터 변환
            ChangeMainDTO changeMainDTO = new ChangeMainDTO(weatherListDTO.getMain());
            // MainDTO 저장
            changeWeatherListDTO.setMain(changeMainDTO);
            // Rain 데이터 저장
            changeWeatherListDTO.setRain(weatherListDTO.getRain());

            responseList.add(changeWeatherListDTO);
        }

        // 저장
        this.list = responseList;
    }
}
