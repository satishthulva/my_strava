package com.satish.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author satish.thulva
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddRun {
    private long userId;
    private double distanceInKm;
    private double durationInHours;
    private String location;
    private String date; // yyyy-mm-dd format
    private String timeOfTheDay; // HH:mm format
}
