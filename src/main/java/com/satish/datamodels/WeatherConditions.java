package com.satish.datamodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author satish.thulva.
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WeatherConditions {
    private double temperature; // celcius
    private double humidity; //
    private double wind; // speed in Kmph
    private String rainIndicator;
}
