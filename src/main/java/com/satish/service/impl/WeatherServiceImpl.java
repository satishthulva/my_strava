package com.satish.service.impl;

import com.satish.service.api.WeatherService;
import com.satish.datamodels.LatLong;
import com.satish.datamodels.WeatherConditions;

import java.util.Date;

/**
 * @author satish.thulva.
 **/
public class WeatherServiceImpl implements WeatherService {

    // TODO : Third party API integration. It's hardcoded right now
    @Override
    public WeatherConditions getWeatherConditions(LatLong latLong, Date timestamp) {
        WeatherConditions weatherConditions = new WeatherConditions();
        weatherConditions.setTemperature(30);
        weatherConditions.setHumidity(80);
        weatherConditions.setWind(10);
        weatherConditions.setRainIndicator("LightRain");
        return weatherConditions;
    }
}
