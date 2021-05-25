package com.satish.datamodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a geographical location on earth in latitude longitude system
 *
 * @author satish.thulva.
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LatLong {
    private double latitude;
    private double longitude;

    @Override
    public String toString() {
        return String.format("%.3f,%.3f", latitude, longitude);
    }

    public static LatLong fromString(String str) {
        String[] fields = str.split(",", -1);
        return new LatLong(Double.parseDouble(fields[0]), Double.parseDouble(fields[1]));
    }

}
