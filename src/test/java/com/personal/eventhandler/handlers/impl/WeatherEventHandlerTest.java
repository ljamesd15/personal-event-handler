package com.personal.eventhandler.handlers.impl;

import com.personal.eventhandler.service.WeatherService;
import org.junit.jupiter.api.Test;

import static com.personal.eventhandler.fixtures.WeatherFixture.TEST_WEATHER_DATA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class WeatherEventHandlerTest {

    @Test
    public void receiveMessageTest() {
        WeatherService weatherService = mock(WeatherService.class);

        WeatherEventHandler underTest = new WeatherEventHandler(weatherService);
        underTest.receiveMessage(TEST_WEATHER_DATA);

        verify(weatherService, times(1)).saveWeatherData(TEST_WEATHER_DATA);
    }
}
