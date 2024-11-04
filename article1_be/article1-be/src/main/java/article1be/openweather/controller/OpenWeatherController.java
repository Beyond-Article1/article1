package article1be.openweather.controller;

import article1be.openweather.dto.OpenWeather5DayDTO;
import article1be.openweather.dto.OpenWeatherDTO;
import article1be.openweather.dto.OpenWeatherAirDTO;
import article1be.openweather.dto.response.ResponseAppointDTO;
import article1be.openweather.dto.response.ResponseMainWeatherDTO;
import article1be.openweather.dto.response.ResponseTodayDTO;
import article1be.openweather.service.OpenWeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
@Tag(name = "날씨", description = "날씨와 관련된 API (time의 형식은 2024-11-04-13:00, lat은 위도, lon은 경도)")
public class OpenWeatherController {

    private final OpenWeatherService openWeatherService;

    // 위도 경도를 파라미터 값으로 불러오기
    // 현재 데이터 출력
    @Operation(summary = "현재 날씨 데이터", description = "위치에 해당하는 현재 날씨 데이터 조회")
    @GetMapping("/now")
    public ResponseEntity<OpenWeatherDTO> getWeather(@RequestParam("lat") String lat,
                                        @RequestParam("lon") String lon) throws UnsupportedEncodingException {
        OpenWeatherDTO weatherData = openWeatherService.getCurrentWeatherData(lat, lon);

        return ResponseEntity.ok(weatherData);
    }

    // 위도 경도를 파라미터 값으로 불러오기
    // 5일간의 데이터 출력
    @Operation(summary = "5일간의 날씨 데이터", description = "위치에 해당하는 5일간의 날씨 데이터 조회")
    @GetMapping("/5day")
    public ResponseEntity<?> get5DayWeather(@RequestParam("lat") String lat,
                                            @RequestParam("lon") String lon) throws UnsupportedEncodingException {

        OpenWeather5DayDTO weatherData = openWeatherService.get5DayWeatherData(lat, lon, 3);

        int dt = weatherData.getList().get(0).getDt();

        return ResponseEntity.ok(weatherData);
    }

    // 현재시간 공기질 수준을 나타냄
    @Operation(summary = "현재 대기 데이터", description = "위치에 해당하는 현재 대기 데이터 조회")
    @GetMapping("/air")
    public ResponseEntity<OpenWeatherAirDTO> getAirData(@RequestParam("lat") String lat,
                                        @RequestParam("lon") String lon) throws UnsupportedEncodingException {

        OpenWeatherAirDTO currentAirData = openWeatherService.getCurrentAirData(lat, lon);

        return ResponseEntity.ok(currentAirData);
    }

    // 테스트 용도로 만든 현재시간 ~ 다음날 00시 이전의 데이터
    @Operation(summary = "현재 ~ 다음날 00시까지의 날씨 데이터", description = "현재 날씨부터 다음날 00시까지의 데이터 조회")
    @GetMapping("/today")
    public ResponseEntity<ResponseTodayDTO> getTodayData(@RequestParam("lat") String lat,
                                          @RequestParam("lon") String lon) throws UnsupportedEncodingException {

        ResponseTodayDTO todayWeatherData = openWeatherService.getTodayWeatherData(lat, lon);

        return ResponseEntity.ok(todayWeatherData);
    }

    // 테스트 용도로 만든 지정시간 ~ 다음날 00시 이전의 데이터
    @Operation(summary = "지정된 날짜의 날씨 및 대기 데이터", description = "지정된 날짜부터 다음날 00시까지의 날씨 및 대기 데이터 조회")
    @GetMapping("/appoint")
    public ResponseEntity<ResponseAppointDTO> getAppointData(@RequestParam("time") String inputTime,
                                            @RequestParam("lat") String lat,
                                            @RequestParam("lon") String lon) throws UnsupportedEncodingException{

        ResponseAppointDTO appointmentWeatherData = openWeatherService.getAppointmentWeatherData(inputTime, lat, lon);

        return ResponseEntity.ok(appointmentWeatherData);
    }

    /**
     * 지정시간 ~ 다음날 00시까지의 데이터 조회 서비스 (날씨코드, 온도, 체감온도, 날씨아이콘, 미세먼지 농도, 초미세먼지 농도, 지정시간 ~ 다음날 00시까지의 (최저기온, 최고기온)
     */
    @Operation(summary = "지정된 날짜의 날씨 및 대기 데이터",
            description = "지정시간부터 다음날 00시까지의 (온도, 체감온도, 날씨코드, 미세먼지 및 초미세먼지 농도, 최저/최고기온 포함) 데이터 조회")
    @GetMapping
    public ResponseEntity<ResponseMainWeatherDTO> getMainWeatherData(@RequestParam("time") String inputTime,
                                                                     @RequestParam("lat") String lat,
                                                                     @RequestParam("lon") String lon) throws UnsupportedEncodingException{

        ResponseMainWeatherDTO mainWeatherData = openWeatherService.getMainWeatherData(inputTime, lat, lon);

        return ResponseEntity.ok(mainWeatherData);
    }
}