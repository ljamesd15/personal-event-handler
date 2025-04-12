package com.personal.eventhandler.handlers.impl;

import com.personal.eventhandler.service.WeatherService;
import org.junit.jupiter.api.Test;

import static com.personal.eventhandler.fixtures.WeatherFixture.TEST_SAVE_WEATHER_REQUEST;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RabbitMqWeatherEventHandlerTest {

    @Test
    public void receiveMessageTest() {
        WeatherService weatherService = mock(WeatherService.class);

        RabbitMqWeatherEventHandler underTest = new RabbitMqWeatherEventHandler(weatherService);
        underTest.receiveMessage(TEST_SAVE_WEATHER_REQUEST);

        verify(weatherService, times(1)).saveWeatherData(TEST_SAVE_WEATHER_REQUEST);
    }
}
