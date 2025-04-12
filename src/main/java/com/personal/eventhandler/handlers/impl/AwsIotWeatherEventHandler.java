package com.personal.eventhandler.handlers.impl;

import com.personal.eventhandler.handlers.EventHandler;
import com.personal.eventhandler.service.WeatherService;
import com.weather.model.external.request.SaveWeatherDataRequest;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AwsIotWeatherEventHandler implements EventHandler<SaveWeatherDataRequest> {

    private final WeatherService weatherService;

    @Autowired
    public AwsIotWeatherEventHandler(final WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @SqsListener("${spring.cloud.aws.sqs.queue-name}")
    public void receiveMessage(SaveWeatherDataRequest weatherData) {
        System.out.println(weatherData);
        this.weatherService.saveWeatherData(weatherData);
    }
}
