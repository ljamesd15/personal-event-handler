package com.personal.eventhandler.handlers.impl;

import com.personal.eventhandler.handlers.EventHandler;
import com.personal.eventhandler.model.weather.WeatherData;
import com.personal.eventhandler.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Handler to process Weather platform events.
 */
@Component
@Slf4j
public class WeatherEventHandler implements EventHandler<WeatherData> {

    private final WeatherService weatherService;

    @Autowired
    public WeatherEventHandler(final WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @RabbitListener(queues = "${rabbitmq.queueName}")
    public void receiveMessage(@Payload final WeatherData weatherData) {
        this.weatherService.saveWeatherData(weatherData);
    }
}
