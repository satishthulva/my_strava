package com.satish.dao.api;

import com.satish.datamodels.LatLong;
import com.satish.datamodels.Run;
import com.satish.datamodels.WeatherConditions;
import com.satish.datamodels.filter.Filter;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author satish.thulva.
 **/
public interface RunDAO {

    void addRun(long userID, double distanceInKm, double durationInHours, LatLong location, Timestamp time,
                WeatherConditions weather, long creator);

    List<Run> fetchAllRuns(long userID, long fromRunId, int pageSize, Filter runFilter);

    long fetchUserRunCount(long userID);

}
