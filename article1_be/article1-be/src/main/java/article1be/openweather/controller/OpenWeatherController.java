package article1be.openweather.controller;

import article1be.openweather.dto.OpenWeather5DayDTO;
import article1be.openweather.dto.OpenWeatherDTO;
import article1be.openweather.dto.OpenWeatherAirDTO;
import article1be.openweather.service.OpenWeatherService;
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
public class OpenWeatherController {

    private final OpenWeatherService openWeatherService;

    // 위도 경도를 파라미터 값으로 불러오기
    // 현재 데이터 출력
    @GetMapping
    public ResponseEntity<?> getWeather(@RequestParam("lat") String lat,
                                        @RequestParam("lon") String lon) throws UnsupportedEncodingException {
        OpenWeatherDTO weatherData = openWeatherService.getCurrentWeatherData(lat, lon);

        return ResponseEntity.ok(weatherData);
    }

    // 위도 경도를 파라미터 값으로 불러오기
    // 5일간의 데이터 출력
    @GetMapping("/5day")
    public ResponseEntity<?> get5DayWeather(@RequestParam("lat") String lat,
                                            @RequestParam("lon") String lon) throws UnsupportedEncodingException {

        OpenWeather5DayDTO weatherData = openWeatherService.get5DayWeatherData(lat, lon);

        int dt = weatherData.getList().get(0).getDt();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(dt), ZoneId.of("Asia/Seoul"));

        log.info("가장 앞의 데이터 시간값 가져오기 {}", localDateTime);

        return ResponseEntity.ok(weatherData);
    }

    @GetMapping("/air")
    public ResponseEntity<?> getAirData() throws UnsupportedEncodingException {

        OpenWeatherAirDTO currentAirData = openWeatherService.getCurrentAirData();

        return ResponseEntity.ok(currentAirData);
    }
}
