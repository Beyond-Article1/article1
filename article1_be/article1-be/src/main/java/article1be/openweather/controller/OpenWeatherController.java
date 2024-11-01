package article1be.openweather.controller;

import article1be.openweather.dto.OpenWeather;
import article1be.openweather.service.OpenWeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class OpenWeatherController {

    private final OpenWeatherService openWeatherService;

    // 위도 경도를 파라미터 값으로 불러오기
    @GetMapping
    public ResponseEntity<?> getWeather(@RequestParam("lat") String lat,
                                        @RequestParam("lon") String lon) throws UnsupportedEncodingException {
        OpenWeather weatherData = openWeatherService.getWeatherData(lat, lon);

        return ResponseEntity.ok(weatherData);
    }
}
