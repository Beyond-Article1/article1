package article1be.openweather.service;

import article1be.common.exception.CustomException;
import article1be.common.exception.ErrorCode;
import article1be.common.utils.DateTimeUtil;
import article1be.openweather.dto.OpenWeather5DayDTO;
import article1be.openweather.dto.OpenWeatherAirDTO;
import article1be.openweather.dto.OpenWeatherDTO;
import article1be.openweather.dto.airs.AirListDTO;
import article1be.openweather.dto.airs.MainAirDTO;
import article1be.openweather.dto.response.ResponseAppointDTO;
import article1be.openweather.dto.response.ResponseMainWeatherDTO;
import article1be.openweather.dto.response.ResponseTodayDTO;
import article1be.openweather.dto.weathers.WeatherListDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenWeatherService {

    // env에 있는 apikey값
    @Value("${open.weather.api.key}")
    private String openWeatherApiKey;

    // 해당 위치 현재시간 날씨 요청하기 위해서 보내는 주소
    @Value("${open.weather.current.api.url}")
    private String openWeatherApiUrl;

    // 해당 위치 5일간의 날씨데이터 요청하는 주소
    @Value("${open.weather.5day.api.url}")
    private String openWeather5DayApiUrl;

    // 해당 위치 현재시간 대기질을 요청하기 위한 주소
    @Value("${open.weather.air.api.url}")
    private String openWeatherAirApiUrl;

    // 해당 위치 5일간의 대기질을 요청하기 위한 주소
    @Value("${open.weather.air.forecast.url}")
    private String openWeatherAirForecastApiUrl;

    /**
     *  현재 날씨 데이터 불러오기
    * */
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


    /**
     * 일정시점으로부터 5일동안 3시간 간격의 날씨 데이터 받기
    * */
    public OpenWeather5DayDTO get5DayWeatherData(String lat, String lon, int cnt) throws UnsupportedEncodingException {

        // 가져올 양 = cnt
        String urlBuilder = openWeather5DayApiUrl + "?" + URLEncoder.encode("lat", "UTF-8") + "=" + lat +
                "&" + URLEncoder.encode("lon", "UTF-8") + "=" + lon +
                "&" + URLEncoder.encode("cnt", "UTF-8") + "=" + cnt +
                "&" + URLEncoder.encode("appid", "UTF-8") + "=" + openWeatherApiKey +
                "&" + URLEncoder.encode("lang", "UTF-8") + "=kr" +
                "&" + URLEncoder.encode("units", "UTF-8") + "=metric";

        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(urlBuilder, OpenWeather5DayDTO.class);
    }

    /**
     *   현재 시간의 미세먼지 농도를 가져오는 서비스
      */
    public OpenWeatherAirDTO getCurrentAirData(String lat, String lon) throws UnsupportedEncodingException {

        String urlBuilder = openWeatherAirApiUrl + "?" + URLEncoder.encode("lat", "UTF-8") + "=" +  lat+
                "&" + URLEncoder.encode("lon", "UTF-8") + "=" + lon +
                "&" + URLEncoder.encode("appid", "UTF-8") + "=" + openWeatherApiKey;

        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(urlBuilder, OpenWeatherAirDTO.class);
    }

    /**
     * 5일간의 미세먼지 농도를 가져오는 서비스
     */
    public OpenWeatherAirDTO get5DayAirData(String lat, String lon, int cnt) throws UnsupportedEncodingException {
        // 가져올 양 = cnt
        String urlBuilder = openWeatherAirForecastApiUrl + "?" + URLEncoder.encode("lat", "UTF-8") + "=" + lat+
                "&" + URLEncoder.encode("lon", "UTF-8") + "=" + lon +
                "&" + URLEncoder.encode("cnt", "UTF-8") + "=" + cnt +
                "&" + URLEncoder.encode("appid", "UTF-8") + "=" + openWeatherApiKey;

        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(urlBuilder, OpenWeatherAirDTO.class);
    }

    // ================== 서비스 로직 ====================== // 
    /**
     *   현재시간에 대한 온도와 체감온도, 미세먼지 농도, 현재 시간~24시까지의 데이터를 반환해주는 서비스 (최고기온, 최저기온, 날씨코드)
      */
    public ResponseTodayDTO getTodayWeatherData(String lat, String lon) throws UnsupportedEncodingException {

        // 위치에 대한 현재 시간에 대한 데이터 가져오기
        OpenWeatherDTO currentWeatherData = getCurrentWeatherData(lat, lon);

        // 현재 시간에 대한 데이터 존재하지 않을 시
        if (currentWeatherData == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_WEATHER_DATA);
        }

        // 반환 데이터 객체 생성 및 현재 데이터 넣기
        ResponseTodayDTO responseTodayDTO = new ResponseTodayDTO(currentWeatherData);

        // 현재 시간 dt
        long nowDt = currentWeatherData.getDt();

        // dt를 LocalDateTime으로 변환
        LocalDateTime nowTime = DateTimeUtil.dtParseToLocalDateTime(nowDt);

        // 다음날 00시 계산
        LocalDateTime tomorrowTime = getTomorrowTime(nowTime);

        // 현재 시간부터 다음날 00시까지 남은 시간 계산하기 (남은 시간 / 3, 다음날 00시 까지의 개수라서 +1)
        int cnt = getCnt(nowTime, tomorrowTime);

        // 미래 날씨 데이터 가져오기
        OpenWeather5DayDTO openWeather5DayDTO = get5DayWeatherData(lat, lon, cnt);

        // 데이터가 없으면
        if (openWeather5DayDTO == null){
            throw new CustomException(ErrorCode.NOT_FOUND_WEATHER_DATA);
        }

        // 가져온 데이터 넣기
        responseTodayDTO.setList(openWeather5DayDTO.getList());

        // 받은데이터 하루 중 최저 온도, 최고 온도 계산하기
        float minTemp = 100, maxTemp = 0;
        for(WeatherListDTO weatherListDTO : openWeather5DayDTO.getList()){
            // 최저온도
            minTemp = Math.min(minTemp, weatherListDTO.getMain().getTemp_max());

            // 최고온도
            maxTemp = Math.max(maxTemp, weatherListDTO.getMain().getTemp_max());
        }

        // 최저온도 넣기
        responseTodayDTO.setLowTemp(minTemp);

        // 최고온도 넣기
        responseTodayDTO.setHighTemp(maxTemp);

        // 으음.. 만약 앞으로 3시간 뒤는 맑음인데, 6시간 뒤에는 비가 오고, 9시간 뒤에는 맑음이라면??
        // 해당 시간의 미세먼지를 넣기 (대기질 수준 자체를 넣는건 어떨까?)
        OpenWeatherAirDTO currentAirData = getCurrentAirData(lat, lon);

        // 대기질 데이터가 없으면
        if (currentAirData == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_AIR_DATA);
        }
        // 공기질 수준 넣기
        responseTodayDTO.setAqi(currentAirData.getList().get(0).getMain().getAqi());

        return responseTodayDTO;
    }

    /**
     * 지정시간 ~ 00시까지의 날씨 데이터 반환 서비스
      */
    public ResponseAppointDTO getAppointmentWeatherData(String inputTime, String lat, String lon) throws UnsupportedEncodingException {

        // 입력 받은 시간을 LocalDateTime으로 변경
        LocalDateTime inputLocalDateTime = DateTimeUtil.stringParseToLocalDateTime(inputTime);

        // 다음날 00시 계산
        LocalDateTime tomorrowTime = getTomorrowTime(inputLocalDateTime);

        // 현재 시간
        LocalDateTime nowTime = LocalDateTime.now();

        // 현재시간 ~ 사용자가 입력한 시간까지의 데이터 개수
        int excludeCnt = getCnt(nowTime, inputLocalDateTime);

        // 현재시간 ~ 사용자가 입력한 시간의 다음날 00시까지의 데이터 개수
        // 현재 시간부터 다음날 00시까지 남은 시간 계산하기 (남은 시간 / 3, 다음날 00시 까지의 개수라서 +1)
        int cnt = getCnt(nowTime, tomorrowTime);


        // 미래 데이터 가져오기
        OpenWeather5DayDTO openWeather5DayDTO = get5DayWeatherData(lat, lon, cnt);

        // 만일 데이터가 없으면 예외 발생
        if (openWeather5DayDTO == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_WEATHER_DATA);
        }

        // 전체 데이터 리스트에서 현재 시간 ~ 사용자가 입력한 시간까지 제외하기
        List<WeatherListDTO> weatherListDTOS = openWeather5DayDTO.getList().subList(excludeCnt, openWeather5DayDTO.getList().size());

        // 반환할 데이터 DTO 인스턴스 할당
        ResponseAppointDTO responseAppointDTO = new ResponseAppointDTO();
        responseAppointDTO.setList(weatherListDTOS);

        // 받은데이터 하루 중 최저 온도, 최고 온도 계산하기
        float minTemp = 100, maxTemp = 0;
        for(WeatherListDTO weatherListDTO : openWeather5DayDTO.getList()){
            // 최저기온
            minTemp = Math.min(minTemp, weatherListDTO.getMain().getTemp_max());

            // 최고기온
            maxTemp = Math.max(maxTemp, weatherListDTO.getMain().getTemp_max());
        }

        // 최저기온
        responseAppointDTO.setLowTemp(minTemp);
        
        // 최고기온
        responseAppointDTO.setHighTemp(maxTemp);

        // 현재 시간 ~ 다음날 00시까지의 대기질 수준 가져오기
        OpenWeatherAirDTO openWeatherAirDTO = get5DayAirData(lat, lon, cnt);

        // 데이터가 없으면 예외처리
        if (openWeatherAirDTO == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_AIR_DATA);
        }

        // 전체 데이터 리스트에서 현재 시간 ~ 사용자가 입력한 시간까지 제외
        List<AirListDTO> airListDTOS = openWeatherAirDTO.getList().subList(excludeCnt, openWeatherAirDTO.getList().size());

        // 제일 많이 나온 대기질 수준 반환
        int aqi = airListDTOS.stream()      // 스트림으로 변환
                .map(AirListDTO::getMain)                   // Main 데이터 꺼내기
                .map(MainAirDTO::getAqi)                    // 꺼낸 Main 데이터에서 대기질 수준 꺼내기
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())) // 빈도 계산하기
                .entrySet()                                 // Map으로 만들기
                .stream()                                   // 스트림으로 변환
                .max(Map.Entry.comparingByValue())          // 가장 많이 나온 값 찾기
                .map(Map.Entry::getKey)                     // 값의 키 꺼내기
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_AIR_DATA));      // 없을 시에 예외 처리
        
        responseAppointDTO.setAqi(aqi);

        return responseAppointDTO;
    }

    /**
     * 지정시간 ~ 다음날 00시까지의 데이터 조회 서비스 (날씨코드, 온도, 체감온도, 날씨아이콘, 미세먼지 농도, 초미세먼지 농도, 지정시간 ~ 다음날 00시까지의 (최저기온, 최고기온)
     */
    public ResponseMainWeatherDTO getMainWeatherData(LocalDateTime inputLocalDateTime, String lat, String lon) throws UnsupportedEncodingException {

        // 입력 받은 시간을 LocalDateTime으로 변경
//        LocalDateTime inputLocalDateTime = DateTimeUtil.stringParseToLocalDateTime(inputTime);

        // 다음날 00시 계산
        LocalDateTime tomorrowTime = getTomorrowTime(inputLocalDateTime);

        // 현재 시간
        LocalDateTime nowTime = LocalDateTime.now();

        // 현재시간 ~ 사용자가 입력한 시간까지의 데이터 개수 (+1이 추가되어 반환된다 따라서 -1)
        int excludeCnt = getCnt(nowTime, inputLocalDateTime);

        // 현재시간인지, 최근 데이터인지 검증 변수
        boolean checkDate = false;

        // 지정시간과 요청시간의 차이가 얼마 나지 않으면 현재 시간을 호출 (&& 3시간단위로 있는 다음 데이터시간과 1시간 이상 차이 나야함)
        int timeDiff = inputLocalDateTime.getHour() / 3;    // 3시간 단위가 아니라면
        if (timeDiff != 0){

            // 다음 데이터가 있는 시간
            LocalDateTime dataDate = inputLocalDateTime.withHour((timeDiff + 1) * 3).withMinute(0).withSecond(0).withNano(0);

            // 남은 시간 (3으로 나눠을때 반내림이 되면 excludeCnt -1)
            float leftTime = (inputLocalDateTime.getHour() + inputLocalDateTime.getMinute() / 60.0f) / 3;

            // 지정시간이 현재시간보다 1시간 이내, 다음 데이터시간보다 1시간 이상일때는 현재시간으로 조회
            if (Duration.between(nowTime, inputLocalDateTime).toHours() <= 1 && Duration.between(inputLocalDateTime, dataDate).toHours() >= 1){
                checkDate = true;
            }
        }

        // 현재시간 ~ 사용자가 입력한 시간의 다음날 00시까지의 데이터 개수
        // 현재 시간부터 다음날 00시까지 남은 시간 계산하기 (남은 시간 / 3, 다음날 00시 까지의 개수라서 +1)
        int cnt = getCnt(nowTime, tomorrowTime) + 1;

        // 미래 데이터 가져오기
        OpenWeather5DayDTO openWeather5DayDTO = get5DayWeatherData(lat, lon, cnt);

        // 만일 데이터가 없으면 예외 발생
        if (openWeather5DayDTO == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_WEATHER_DATA);
        }

        // 전체 데이터 리스트에서 현재 시간 ~ 사용자가 입력한 시간까지 제외하기
        List<WeatherListDTO> weatherListDTOS = openWeather5DayDTO.getList().subList(excludeCnt, openWeather5DayDTO.getList().size());

        // 반환할 객체 인스턴스 할당
        ResponseMainWeatherDTO responseMainWeatherDTO = new ResponseMainWeatherDTO();

        if (checkDate){
            // 지정시간이 현재시간과 비슷하다면 현재시간 기입
            OpenWeatherDTO currentWeatherData = getCurrentWeatherData(lat, lon);
            responseMainWeatherDTO.setNowWeatherCode(currentWeatherData.getWeather().get(0).getId());
            responseMainWeatherDTO.setNowWeatherDescription(currentWeatherData.getWeather().get(0).getDescription());
            responseMainWeatherDTO.setNowTemp(currentWeatherData.getMain().getTemp());
            responseMainWeatherDTO.setNowFeelsLike(currentWeatherData.getMain().getFeels_like());
            responseMainWeatherDTO.setNowWeatherIcon(currentWeatherData.getWeather().get(0).getIcon());

            // 현재시간 기반
            LocalDateTime callTime = DateTimeUtil.dtParseToLocalDateTime(currentWeatherData.getDt());
            responseMainWeatherDTO.setNowTime(callTime);
        } else {
            // 지정시간에 가장 가까운 (날씨코드, 아이콘, 온도, 체감온도) 기입
            responseMainWeatherDTO.setNowWeatherCode(weatherListDTOS.get(0).getWeather().get(0).getId());
            responseMainWeatherDTO.setNowWeatherIcon(weatherListDTOS.get(0).getWeather().get(0).getIcon());
            responseMainWeatherDTO.setNowWeatherDescription(weatherListDTOS.get(0).getWeather().get(0).getDescription());
            responseMainWeatherDTO.setNowTemp(weatherListDTOS.get(0).getMain().getTemp());
            responseMainWeatherDTO.setNowFeelsLike(weatherListDTOS.get(0).getMain().getFeels_like());

            // 가장가깝게 가져온 시간 기입 dt변환
            LocalDateTime appointmentTime = DateTimeUtil.dtParseToLocalDateTime(weatherListDTOS.get(0).getDt());
            responseMainWeatherDTO.setNowTime(appointmentTime);
        }

        // 이후 시간들의 데이터 기입
        responseMainWeatherDTO.setList(weatherListDTOS);

        // 받은데이터 하루 중 최저 온도, 최고 온도 계산하기
        float minTemp = 100, maxTemp = 0;
        for(WeatherListDTO weatherListDTO : openWeather5DayDTO.getList()){
            // 최저기온
            minTemp = Math.min(minTemp, weatherListDTO.getMain().getTemp_max());

            // 최고기온
            maxTemp = Math.max(maxTemp, weatherListDTO.getMain().getTemp_max());
        }

        // 최저기온
        responseMainWeatherDTO.setLowTemp(minTemp);

        // 최고기온
        responseMainWeatherDTO.setHighTemp(maxTemp);

        // 현재 시간 ~ 다음날 00시까지의 대기질 수준 가져오기
        OpenWeatherAirDTO openWeatherAirDTO = get5DayAirData(lat, lon, cnt);

        // 데이터가 없으면 예외처리
        if (openWeatherAirDTO == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_AIR_DATA);
        }

        // 전체 데이터 리스트에서 현재 시간 ~ 사용자가 입력한 시간까지 제외
        List<AirListDTO> airListDTOS = openWeatherAirDTO.getList().subList(excludeCnt, openWeatherAirDTO.getList().size());

        // 지정시간 이후 가장 가까운 대기 데이터의 미세먼지, 초미세먼지 농도 기입
        responseMainWeatherDTO.setPm2_5(airListDTOS.get(0).getComponents().getPm2_5());
        responseMainWeatherDTO.setPm10(airListDTOS.get(0).getComponents().getPm10());

        return responseMainWeatherDTO;
    }

    /**
     * 다음날 00시 구하기
     */
    private LocalDateTime getTomorrowTime(LocalDateTime inputLocalDateTime){
        // 다음날 00시 계산
        return inputLocalDateTime.plusDays(1).toLocalDate().atStartOfDay();
    }

    /**
     * 지정 시간 ~ 다음날 00시 까지의 갯수 구하기
     */
    private int getCnt(LocalDateTime inputTime, LocalDateTime tomorrowTime) {
        // 현재 시간부터 다음날 00시까지 남은 시간 계산하기 (남은 시간 / 3, 다음날 00시 까지의 개수라서 +1)
        int times = (int) Math.floor(Duration.between(inputTime, tomorrowTime).toHours());

        return times / 3;
    }

}