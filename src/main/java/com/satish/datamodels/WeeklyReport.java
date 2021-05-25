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
 * We can choose to use ISO 8601 week numbering.
 *
 * @author satish.thulva.
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WeeklyReport implements AccessControlled {
    private long reportId;
    private long userId;

    private double totalDistanceInKm; // total distance covered in the week
    private double totalTimeInHours; // total time logged in the week
    private int numberOfRuns; // total number of runs logged in the week

    private double avgSpeedInKmph; // speed on an avg this week
    private double avgDistancePerRunInKm; // distance on an avg per run this week
    private double avgTimePerRunInHour; // time on an avg per run this week

    private int weekOfTheYear; // ISO 8601 ?
    private int year;
    private Timestamp startingDayOfTheWeek; // For UI ?

    @Override
    public long getId() {
        return reportId;
    }

    @Override
    public Flavour getFlavour() {
        return Flavour.REPORT;
    }
}
