package com.satish.datamodels;


import com.satish.accesscontrol.AccessControlled;
import com.satish.accesscontrol.Flavour;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @author satish.thulva.
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Run implements AccessControlled {
    private long id;
    private UserID userID;
    private double distanceInKm;
    private double durationInHours;
    private LatLong location;
    private String date; // yyyy-mm-dd format
    private String timeOfTheDay; // HH:mm format
    private Timestamp time;
    private WeatherConditions weather;

    @Override
    public Flavour getFlavour() {
        return Flavour.RUN;
    }
}
