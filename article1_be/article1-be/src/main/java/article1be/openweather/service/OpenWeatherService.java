package article1be.openweather.service;

import article1be.openweather.dto.OpenWeather;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
public class OpenWeatherService {

    // 프로퍼티에 있는 apikey값
    @Value("${open.weather.api.key}")
    private String openWeatherApiKey;

    /**
     * https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
     * https://api.openweathermap.org/data/3.0/onecall?lat={lat}&lon={lon}&exclude={part}&appid={API key}
     */
    // 요청하기 위해서 보내는 주소
//    @Value("${open.weather.api.url")
    private String openWeatherApiUrl = "https://api.openweathermap.org/data/2.5/weather";

    // 날씨 데이터 불러오기
    public OpenWeather getWeatherData() throws UnsupportedEncodingException {
        StringBuilder urlBuilder = new StringBuilder(openWeatherApiUrl);

//        urlBuilder.append("?" + URLEncoder.encode("q", "UTF-8") + "=Seoul");
        urlBuilder.append("?" + URLEncoder.encode("lat", "UTF-8") + "=37.22296455822422");
        urlBuilder.append("&" + URLEncoder.encode("lon", "UTF-8") + "=127.05652887872013");
        urlBuilder.append("&" + URLEncoder.encode("appid", "UTF-8") + "=" + openWeatherApiKey);
        urlBuilder.append("&" + URLEncoder.encode("lang", "UTF-8") + "=kr");
        urlBuilder.append("&" + URLEncoder.encode("units", "UTF-8") + "=metric");

        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(urlBuilder.toString(), OpenWeather.class);
    }

}
