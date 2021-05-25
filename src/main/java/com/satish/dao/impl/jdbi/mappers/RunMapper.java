package com.satish.dao.impl.jdbi.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.satish.datamodels.LatLong;
import com.satish.datamodels.Run;
import com.satish.datamodels.UserID;
import com.satish.datamodels.WeatherConditions;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author satish.thulva
 **/
@Slf4j
public class RunMapper implements RowMapper<Run> {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ThreadLocal<SimpleDateFormat> dateFormat = ThreadLocal.withInitial(
            () -> new SimpleDateFormat("yyyy-MM-dd"));

    @Override
    public Run map(ResultSet rs, StatementContext ctx) throws SQLException {
        try {
            long runId = rs.getLong("run_id");
            long userId = rs.getLong("user_id");
            double distance = rs.getDouble("distance_in_km");
            double duration = rs.getDouble("duration_in_hours");
            LatLong latLong = LatLong.fromString(rs.getString("lat_lng"));
            Timestamp runTime = rs.getTimestamp("run_time");
            WeatherConditions weather = objectMapper.readValue(rs.getString("weather_conditions"),
                    WeatherConditions.class);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(runTime);
            return new Run(runId, new UserID(userId), distance, duration, latLong, dateFormat.get().format(runTime),
                    calendar.get(Calendar.HOUR_OF_DAY) + "" + calendar.get(Calendar.MINUTE), runTime, weather);
        } catch (IOException e) {
            log.error("Jackson error while reading weather data back", e);
            throw new SQLException(e);
        }
    }
}
