package com.satish.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Configuration for the app
 *
 * @author satish.thulva.
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Config {
    private DatabaseConfig db;
    private WeatherApiConfig weather;
    private AppConfig app;
}
