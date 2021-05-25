package com.satish.service.impl;

import com.satish.accesscontrol.Actor;
import com.satish.datamodels.*;
import com.satish.service.api.WeatherService;
import com.satish.dao.api.RunDAO;
import com.satish.datamodels.filter.Filter;
import com.satish.service.api.RunService;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author satish.thulva.
 **/
public class RunServiceImpl implements RunService {

    private final RunDAO runDAO;
    private final WeatherService weatherService;
    private static ThreadLocal<DateFormat> dateFormat = ThreadLocal.withInitial(
            () -> new SimpleDateFormat("yyyy-MM-dd"));

    @Inject
    public RunServiceImpl(RunDAO runDAO, WeatherService weatherService) {
        this.runDAO = runDAO;
        this.weatherService = weatherService;
    }

    @Override
    public void createRun(Actor actor, UserID userId, double distanceInKm, double durationInHours, LatLong location,
                          String date, String timeOfTheDay) {
        Timestamp timestamp = toTimestamp(date, timeOfTheDay);
        WeatherConditions weatherConditions = weatherService.getWeatherConditions(location, timestamp);
        runDAO.addRun(userId.getId(), distanceInKm, durationInHours, location,
                timestamp, weatherConditions, actor.getId());
    }

    private Timestamp toTimestamp(String date, String timeOfTheDay) {
        Date dt;
        try {
            dt = dateFormat.get().parse(date);
        } catch (ParseException e) {
            dt = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        int hhmm = Integer.parseInt(timeOfTheDay);
        calendar.set(Calendar.HOUR_OF_DAY, hhmm / 100);
        calendar.set(Calendar.MINUTE, hhmm % 100);
        return new Timestamp(calendar.getTimeInMillis());
    }

    @Override
    public PaginatedResult<Run> listRuns(Actor actor, UserID userId, Filter filter, long offset, int limit) {
        List<Run> runs = runDAO.fetchAllRuns(userId.getId(), offset, limit, filter);
        return new PaginatedResult<>(runs, runDAO.fetchUserRunCount(userId.getId()), runs.size());
    }

    @Override
    public WeeklyReport getWeeklyRunReport(Actor actor, UserID userId, int weekNumber) {
        return null;
    }

    @Override
    public PaginatedResult<WeeklyReport> listWeeklyReports(Actor actor, UserID userId, int offset, int limit) {
        return null;
    }

    @Override
    public MonthlyReport getMonthlyRunReport(String actor, UserID userId, int monthNumber) {
        return null;
    }

    @Override
    public PaginatedResult<WeeklyReport> listMonthlyReports(Actor actor, UserID userId, int offset, int limit) {
        return null;
    }
}
