package com.personal.eventhandler.service;


import com.weather.model.external.request.SaveWeatherDataRequest;

public interface WeatherService {

    boolean saveWeatherData(SaveWeatherDataRequest weatherData);
}
