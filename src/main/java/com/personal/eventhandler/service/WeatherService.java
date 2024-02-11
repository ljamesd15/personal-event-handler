package com.personal.eventhandler.service;

import com.personal.eventhandler.model.weather.WeatherData;

public interface WeatherService {

    boolean saveWeatherData(WeatherData weatherData);
}
