package com.personal.eventhandler.service;


import com.weather.model.external.WeatherData;

public interface WeatherService {

    boolean saveWeatherData(WeatherData weatherData);
}
