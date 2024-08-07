package com.personal.eventhandler.fixtures;

import com.weather.model.external.SensorMetadata;
import com.weather.model.external.WeatherData;
import com.weather.model.external.enums.Direction;
import com.weather.model.external.request.SaveWeatherDataRequest;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import static java.time.ZoneOffset.UTC;

public interface WeatherFixture {

    String TEST_SAVE_WEATHER_DATA_URI = "/saveWeatherData";
    String TEST_JWT_TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJZSmRtYUR2VlRKeHRjV1JDdmtNaWtjOG9FTGdBVk5jeiIsImV4cCI6MTQ0MjQzMDA1NCwibmJmIjoxNDQyNDI2NDU0LCJpYXQiOjE0NDI0MjY0NTR9.WuLdHyvZGj2UAsnBl6YF9A4NqGQpaDftHjX18ooK8YY";

    ZonedDateTime TEST_ZONED_TIME = ZonedDateTime.of(2024, 1, 31, 12, 10, 8, 6, UTC);
    LocalDateTime TEST_LOCAL_TIME = TEST_ZONED_TIME.toLocalDateTime();
    String TEST_WEATHER_DATA_ID = "66678d1d34ead05a95aa399c";
    double TEST_HUMIDITY = 61.6;
    double TEST_PRESSURE = 1024;
    double TEST_TEMPERATURE = 25.9;
    double TEST_LUMINOSITY = 12000;
    double TEST_UV_INDEX = 5.2;
    double TEST_WIND_SPEED = 5.7;
    double TEST_LATITUDE = 12.2;
    double TEST_LONGITUDE = 34.1;
    String TEST_SENSOR_ID = "Sensor1";
    String TEST_TAG_1 = "Tag1";
    String TEST_TAG_2 = "Tag2";
    String TEST_LOCATION = "Greenhouse";

    SensorMetadata TEST_SENSOR_METADATA = SensorMetadata.builder()
            .latitude(TEST_LATITUDE)
            .longitude(TEST_LONGITUDE)
            .sensorId(TEST_SENSOR_ID)
            .tags(List.of(TEST_TAG_1, TEST_TAG_2))
            .location(TEST_LOCATION)
            .build();

    WeatherData TEST_WEATHER_DATA = WeatherData.builder()
            .id(TEST_WEATHER_DATA_ID)
            .time(TEST_ZONED_TIME)
            .humidity(TEST_HUMIDITY)
            .pressure(TEST_PRESSURE)
            .temperature(TEST_TEMPERATURE)
            .luminosity(TEST_LUMINOSITY)
            .uvIndex(TEST_UV_INDEX)
            .windDirection(Direction.N)
            .windSpeed(TEST_WIND_SPEED)
            .sensorMetadata(TEST_SENSOR_METADATA)
            .build();

    SaveWeatherDataRequest TEST_SAVE_WEATHER_REQUEST = SaveWeatherDataRequest.builder()
            .time(TEST_ZONED_TIME)
            .humidity(TEST_HUMIDITY)
            .pressure(TEST_PRESSURE)
            .temperature(TEST_TEMPERATURE)
            .luminosity(TEST_LUMINOSITY)
            .uvIndex(TEST_UV_INDEX)
            .windDirection(Direction.N)
            .windSpeed(TEST_WIND_SPEED)
            .sensorMetadata(TEST_SENSOR_METADATA)
            .build();
}
