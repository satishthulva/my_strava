package com.satish.dao.impl.jdbi.mappers;

import com.satish.datamodels.WeeklyReport;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * @author satish.thulva.
 **/
public class WeeklyReportMapper implements RowMapper<WeeklyReport> {
    @Override
    public WeeklyReport map(ResultSet rs, StatementContext ctx) throws SQLException {
        long reportId = rs.getLong("report_id");
        long userId = rs.getLong("user_id");
        double totalDistance = rs.getDouble("total_distance_in_km");
        double totalTime = rs.getDouble("total_time_in_hours");
        int numRuns = rs.getInt("number_of_runs");
        double avgSpeed = rs.getDouble("avg_speed_kmph");
        double avgDistancePerRun = rs.getDouble("avg_distance_per_run_in_km");
        double avgTimePerRun = rs.getDouble("avg_time_per_run_in_hour");
        int weekOfTheYear = rs.getInt("week_of_the_year");
        int runYear = rs.getInt("run_year");
        Timestamp firstDayOfWeek = rs.getTimestamp("week_first_day");
        return new WeeklyReport(reportId, userId, totalDistance, totalTime, numRuns, avgSpeed, avgDistancePerRun,
                avgTimePerRun, weekOfTheYear, runYear, firstDayOfWeek);
    }
}
