package article1be.openweather.dto;

import lombok.Data;

import java.util.List;

@Data
public class OpenWeather5DayDTO {
    private String cod;
    private int message;
    private int cnt;
    private List<WeatherList> list;
    private City city;

    @Data
    public static class WeatherList {
        private int dt;
        private Main main;
        private List<Weather> weather;
        private Clouds clouds;
        private Wind wind;
        private int visibility;
        private float pop;
        private Rain rain;
        private Sys sys;
        private String dt_txt;
    }

    @Data
    public static class Main {
        private float temp;
        private float feels_like;
        private float temp_min;
        private float temp_max;
        private int pressure;
        private float humidity;
        private float sea_level;
        private float grnd_level;
    }

    @Data
    public static class Weather {
        private int id;
        private String main;
        private String description;
        private String icon;
    }

    @Data
    public static class Clouds {
        private int all;
    }

    @Data
    public static class Wind {
        private float speed;
        private int deg;
        private float gust;
    }

    @Data
    public static class Rain {
        private float _3h;
    }

    @Data
    public static class Sys {
        private String pod;
    }

    @Data
    public static class City {
        private int id;
        private String name;
        private Coord coord;
        private String country;
        private int population;
        private int timezone;
        private int sunrise;
        private int sunset;
    }

    @Data
    public static class Coord {
        private float lat;
        private float lon;
    }
}
