package com.satish.dao.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.satish.dao.api.RunDAO;
import com.satish.datamodels.LatLong;
import com.satish.datamodels.Run;
import com.satish.datamodels.WeatherConditions;
import com.satish.datamodels.filter.Filter;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import javax.inject.Inject;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author satish.thulva.
 **/
@Slf4j
public class RunDAOImpl extends AbstractDAO implements RunDAO {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    public RunDAOImpl(DBDataSource dbDataSource) {
        super(dbDataSource);
    }

    @Override
    public void addRun(long userID, double distanceInKm, double durationInHours, LatLong location, Timestamp time,
                       WeatherConditions weather, long creator) {
        try {
            jdbi.useExtension(RunDAOJdbi.class,
                    e -> e.insertRun(userID, distanceInKm, durationInHours, location.toString(),
                            time, objectMapper.writeValueAsString(weather), creator));
        } catch (IOException e) {
            log.error("Error writing run entry", e);
        }
    }

    @Override
    public List<Run> fetchAllRuns(long userID, long fromRunId, int pageSize, Filter runFilter) {
        return runFilter != null
               ? jdbi.withExtension(RunDAOJdbi.class,
                e -> e.listRunsWithFilter(fromRunId, runFilter.toQueryComponent(), pageSize))
               : jdbi.withExtension(RunDAOJdbi.class, e -> e.listRunsNoFilter(fromRunId, pageSize));
    }

    @Override
    public long fetchUserRunCount(long userID) {
        return jdbi.withExtension(RunDAOJdbi.class, e -> e.findUserRunCount(userID));
    }

    private interface RunDAOJdbi {
        @SqlUpdate("INSERT INTO user_runs(user_id, distance_in_km, duration_in_hours, lat_lng, run_time, " +
                           "weather_conditions, created_by) VALUES (:userId, :distance, :durationInh, :loc, " +
                           ":runTime, :weather, :creator)")
        void insertRun(@Bind("userId") long userId, @Bind("distance") double distance,
                       @Bind("durationInh") double hours, @Bind("loc") String location,
                       @Bind("runTime") Timestamp runTime, @Bind("weather") String weather,
                       @Bind("creator") long creator);

        @SqlQuery("SELECT * FROM user_runs WHERE run_id > :runId AND :filterCondition ORDER BY run_id LIMIT " +
                          ":numEntries")
        List<Run> listRunsWithFilter(@Bind("runId") long runId, @Bind("filterCondition") String filterCondition,
                                     @Bind("numEntries") int numEntries);

        @SqlQuery("SELECT * FROM user_runs WHERE run_id > :runId ORDER BY run_id LIMIT :numEntries")
        List<Run> listRunsNoFilter(@Bind("runId") long runId, @Bind("numEntries") int numEntries);

        @SqlQuery("SELECT COUNT(*) FROM user_runs WHERE user_id = :userId")
        long findUserRunCount(@Bind("userId") long userId);

    }
}
