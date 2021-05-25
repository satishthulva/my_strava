package com.satish.datamodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A location wrapped with a name. Could be area/city that can be
 * obtained third party maps APIs or User can tag each location him/herself
 *
 * @author satish.thulva.
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Location {
    private LatLong latLong;
    private String name;
}
