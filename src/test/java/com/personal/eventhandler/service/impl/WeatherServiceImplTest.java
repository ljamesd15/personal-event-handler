package com.personal.eventhandler.service.impl;

import com.personal.eventhandler.service.WeatherService;
import com.personal.eventhandler.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static com.personal.eventhandler.fixtures.WeatherFixture.TEST_JWT_TOKEN;
import static com.personal.eventhandler.fixtures.WeatherFixture.TEST_SAVE_WEATHER_DATA_URI;
import static com.personal.eventhandler.fixtures.WeatherFixture.TEST_SAVE_WEATHER_REQUEST;
import static com.personal.eventhandler.utils.Constants.AUTHORIZATION_HEADER;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WeatherServiceImplTest {

    @Mock
    JwtUtil jwtUtil;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(jwtUtil.getWeatherToken()).thenReturn(TEST_JWT_TOKEN);
    }

    @Test
    public void saveWeatherDataTest() {
        RestClient restClient = mock(RestClient.class);
        RestClient.RequestBodyUriSpec uriSpec = mock(RestClient.RequestBodyUriSpec.class);
        RestClient.RequestBodySpec bodySpec = mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
        ResponseEntity responseEntity = mock(ResponseEntity.class);
        when(restClient.post()).thenReturn(uriSpec);
        when(uriSpec.uri(TEST_SAVE_WEATHER_DATA_URI)).thenReturn(bodySpec);
        when(bodySpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(bodySpec);
        when(bodySpec.header(AUTHORIZATION_HEADER, TEST_JWT_TOKEN)).thenReturn(bodySpec);
        when(bodySpec.body(TEST_SAVE_WEATHER_REQUEST)).thenReturn(bodySpec);
        when(bodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(responseEntity);
        when(responseEntity.getStatusCode()).thenReturn(HttpStatusCode.valueOf(200));

        WeatherService underTest = new WeatherServiceImpl(restClient, jwtUtil);
        boolean result = underTest.saveWeatherData(TEST_SAVE_WEATHER_REQUEST);
        assertTrue(result);
    }


    @Test
    public void saveWeatherDataTest_withRequestFailure() {
        RestClient restClient = mock(RestClient.class);
        RestClient.RequestBodyUriSpec uriSpec = mock(RestClient.RequestBodyUriSpec.class);
        RestClient.RequestBodySpec bodySpec = mock(RestClient.RequestBodySpec.class);
        when(restClient.post()).thenReturn(uriSpec);
        when(uriSpec.uri(TEST_SAVE_WEATHER_DATA_URI)).thenReturn(bodySpec);
        when(bodySpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(bodySpec);
        when(bodySpec.header(AUTHORIZATION_HEADER, TEST_JWT_TOKEN)).thenReturn(bodySpec);
        when(bodySpec.body(TEST_SAVE_WEATHER_REQUEST)).thenReturn(bodySpec);
        when(bodySpec.retrieve()).thenThrow(WebClientResponseException.class);

        WeatherService underTest = new WeatherServiceImpl(restClient, jwtUtil);
        boolean result = underTest.saveWeatherData(TEST_SAVE_WEATHER_REQUEST);
        assertFalse(result);
    }
}
