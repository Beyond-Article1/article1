package article1be.openweather.service;

import article1be.openweather.dto.OpenWeather5DayDTO;
import article1be.openweather.dto.OpenWeatherDTO;
import article1be.openweather.dto.OpenWeatherAirDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
public class OpenWeatherService {

    // env에 있는 apikey값
    @Value("${open.weather.api.key}")
    private String openWeatherApiKey;

    /**
     * https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
     * https://api.openweathermap.org/data/3.0/onecall?lat={lat}&lon={lon}&exclude={part}&appid={API key}
     */
    // 해당 위치 날씨 요청하기 위해서 보내는 주소
    @Value("${open.weather.current.api.url}")
    private String openWeatherApiUrl;

    // 해당 위치 5일간의 날씨데이터 요청하는 주소
    @Value("${open.weather.5day.api.url}")
    private String openWeather5DayApiUrl;

    @Value("${open.weather.air.api.url}")
    private String openWeatherAirApiUrl;

    // 현재 날씨 데이터 불러오기
    public OpenWeatherDTO getCurrentWeatherData(String lat, String lon) throws UnsupportedEncodingException {

//        urlBuilder.append("?" + URLEncoder.encode("q", "UTF-8") + "=Seoul");
        String urlBuilder = openWeatherApiUrl + "?" + URLEncoder.encode("lat", "UTF-8") + "=" + lat +
                "&" + URLEncoder.encode("lon", "UTF-8") + "=" + lon +
                "&" + URLEncoder.encode("appid", "UTF-8") + "=" + openWeatherApiKey +
                "&" + URLEncoder.encode("lang", "UTF-8") + "=kr" +
                "&" + URLEncoder.encode("units", "UTF-8") + "=metric";

        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(urlBuilder, OpenWeatherDTO.class);
    }


    // 일정시점으로부터 5일동안 3시간 간격의 날씨 데이터 받기
    public OpenWeather5DayDTO get5DayWeatherData(String lat, String lon) throws UnsupportedEncodingException {

        // 가져올 양
        int cnt = 3;

        String urlBuilder = openWeather5DayApiUrl + "?" + URLEncoder.encode("lat", "UTF-8") + "=" + lat +
                "&" + URLEncoder.encode("lon", "UTF-8") + "=" + lon +
                "&" + URLEncoder.encode("cnt", "UTF-8") + "=" + cnt +
                "&" + URLEncoder.encode("appid", "UTF-8") + "=" + openWeatherApiKey +
                "&" + URLEncoder.encode("lang", "UTF-8") + "=kr" +
                "&" + URLEncoder.encode("units", "UTF-8") + "=metric";

        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(urlBuilder, OpenWeather5DayDTO.class);
    }

    // 미세먼지 농도를 가져오는 서비스
    public OpenWeatherAirDTO getCurrentAirData(String lat, String lon) throws UnsupportedEncodingException {

        String urlBuilder = openWeatherAirApiUrl + "?" + URLEncoder.encode("lat", "UTF-8") + "=" +  lat+
                "&" + URLEncoder.encode("lon", "UTF-8") + "=" + lon +
                "&" + URLEncoder.encode("appid", "UTF-8") + "=" + openWeatherApiKey;

        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(urlBuilder, OpenWeatherAirDTO.class);
    }
}