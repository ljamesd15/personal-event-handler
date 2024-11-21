package com.personal.eventhandler.service.impl;

import com.personal.eventhandler.service.WeatherService;
import com.personal.eventhandler.utils.JwtUtil;
import com.weather.model.external.request.SaveWeatherDataRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static com.personal.eventhandler.utils.Constants.AUTHORIZATION_HEADER;


@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    private static final String SAVE_WEATHER_PATH = "/weather/saveWeatherData";

    private final RestClient weatherClient;
    private final JwtUtil jwtUtil;

    @Autowired
    public WeatherServiceImpl(final @Qualifier("WeatherService") RestClient restClient,
                              final JwtUtil jwtUtil) {
        this.weatherClient = restClient;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean saveWeatherData(final SaveWeatherDataRequest weatherData) {
        ResponseEntity<Void> response;
        try {
            response = this.weatherClient.post()
                    .uri(SAVE_WEATHER_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTHORIZATION_HEADER, this.jwtUtil.getWeatherToken())
                    .body(weatherData)
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
