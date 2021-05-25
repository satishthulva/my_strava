package com.satish.service.api;

import com.satish.datamodels.LatLong;
import com.satish.datamodels.WeatherConditions;

import java.util.Date;

/**
 * Abstraction over third party weather information providers. Changing the provider should not have an
 * impact on the way this service is used internally
 *
 * @author satish.thulva.
 **/
public interface WeatherService {

    /**
     * Weather condition for a given location at a given point of time
     *
     * @param latLong   Geographic coordinated of a location on earth
     * @param timestamp UTC time
     * @return Weather condition
     */
    WeatherConditions getWeatherConditions(LatLong latLong, Date timestamp);

}
