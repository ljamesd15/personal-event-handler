package com.personal.eventhandler.service.impl;

import com.personal.eventhandler.model.weather.SaveWeatherDataRequest;
import com.personal.eventhandler.model.weather.WeatherData;
import com.personal.eventhandler.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    private static final String SAVE_WEATHER_PATH = "/saveWeatherData";

    private final RestClient weatherClient;

    @Autowired
    public WeatherServiceImpl(final @Qualifier("WeatherService") RestClient restClient) {
        this.weatherClient = restClient;
    }

    @Override
    public boolean saveWeatherData(final WeatherData weatherData) {
        final SaveWeatherDataRequest body = SaveWeatherDataRequest.builder()
                .weatherData(weatherData)
                .build();
        ResponseEntity<Void> response;
        try {
            response = this.weatherClient.post()
                    .uri(SAVE_WEATHER_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();
        } catch (WebClientResponseException ex) {
            log.error(ex.getMessage());
            return false;
        }
        log.info("Status code: {}", response.getStatusCode());
        return true;
    }
}
