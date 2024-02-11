package com.personal.eventhandler.model.weather;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SaveWeatherDataRequest {
    private final WeatherData weatherData;
}
