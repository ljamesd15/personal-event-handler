package com.personal.eventhandler.model.weather;

import com.personal.eventhandler.model.enums.Direction;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class WeatherData {
    private final ZonedDateTime time;
    private final Double temperature;
    private final Double pressure;
    private final Double humidity;
    private final Double windSpeed;
    private final Double luminosity;
    private final Double uvIndex;
    private final Direction windDirection;
    private final SensorMetadata sensorMetadata;
}
