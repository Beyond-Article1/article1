package article1be.openweather.controller;

import article1be.openweather.dto.OpenWeather;
import article1be.openweather.service.OpenWeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class OpenWeatherController {

    private final OpenWeatherService openWeatherService;

    @GetMapping
    public ResponseEntity<?> getWeather() throws UnsupportedEncodingException {
        OpenWeather weatherData = openWeatherService.getWeatherData();

        return ResponseEntity.ok(weatherData);
    }
}
