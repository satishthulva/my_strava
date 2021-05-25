package com.satish.datamodels;

import com.satish.accesscontrol.AccessControlled;
import com.satish.accesscontrol.Flavour;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Summary statistics for all runs completed in a week.
 * More statistics can be added as per requirement.
 *
 * @author satish.thulva.
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MonthlyReport implements AccessControlled {
    private long id;
    private double totalDistanceInKm;
    private double totalTimeInHours;
    private int numberOfRuns;

    private double avgSpeedInKmph;
    private double avgDistancePerWeek;

    private int month; // numbering starts from 0 similar to Java Calendar
    private Timestamp startingDayOfTheMonth; // For UI ?

    @Override
    public Flavour getFlavour() {
        return Flavour.REPORT;
    }
}
