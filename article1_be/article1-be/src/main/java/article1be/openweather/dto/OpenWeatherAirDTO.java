package article1be.openweather.dto;

import article1be.openweather.dto.airs.AirListDTO;
import article1be.openweather.dto.weathers.CoordDTO;
import lombok.Data;

import java.util.List;

@Data
public class OpenWeatherAirDTO {

    private CoordDTO coord;
    private List<AirListDTO> list; // AirList는 여러 개일 수 있음.

}