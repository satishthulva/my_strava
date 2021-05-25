package com.satish.service.api;

import com.satish.accesscontrol.Actor;
import com.satish.accesscontrol.Requires;
import com.satish.accesscontrol.SelfAccess;
import com.satish.datamodels.*;
import com.satish.datamodels.filter.Filter;

/**
 * @author satish.thulva.
 **/
public interface RunService {

    /**
     * Create a new run entry for user with given info. The time related fields are assumed to be in local timeZone
     * of the specified location
     *
     * @param actor        Unique id of the user adding the entry
     * @param userId       Unique id of the user the entry is being added for
     * @param distanceInKm Run distance in KM
     * @param location     Geographical location where the run ended. This is used to extract the TimeZone info
     * @param date         Date in YYYY-MM-DD Java format in local timeZone of the specified location
     * @param timeOfTheDay Time of the day in HHMM format in local timeZone of the specified location
     */
    @Requires(Role.ADMIN)
    @SelfAccess
    void createRun(Actor actor, UserID userId, double distanceInKm, double durationInHours, LatLong location,
                   String date, String timeOfTheDay);

    /**
     * Fetch the next page of run entries made for user `userId' which pass given filter and fall in to the
     * page identified by given offset and limit
     *
     * @param actor  UniqueId of the acting user
     * @param userId UniqueId of the user to get runs info for
     * @param filter Filter to be applied on the run entries list
     * @param offset Number of run entries scrolled past till now in the listing
     * @param limit  Number of entries to be fetched now, if available
     * @return Max `limit' number of entries based on the number of remaining entries left in the listing. If there are
     * more than `limit' entries, limit number of entries would be returned. If there are less than limit number of
     * entries, all of those will be returned
     */
    @Requires(Role.ADMIN)
    @SelfAccess
    PaginatedResult<Run> listRuns(Actor actor, UserID userId, Filter filter, long offset, int limit);

    /**
     * Fetch the run report of given user for given week of the year
     *
     * @param actor      UniqueId of the acting user
     * @param userId     UniqueId of the user to get runs info for
     * @param weekNumber ISO 8601 week number of the year
     * @return Run report for given user and week combination
     */
    @Requires(Role.ADMIN)
    @SelfAccess
    WeeklyReport getWeeklyRunReport(Actor actor, UserID userId, int weekNumber);

    /**
     * Fetch next page in weekly report list similar to {@link RunService#listRuns(Actor, UserID, Filter, long, int)}
     */
    @Requires(Role.ADMIN)
    @SelfAccess
    PaginatedResult<WeeklyReport> listWeeklyReports(Actor actor, UserID userId, int offset, int limit);

    /**
     * Fetch the run report of given user for given Month of the year
     *
     * @param actor       UniqueId of the acting user
     * @param userId      UniqueId of the user to get runs info for
     * @param monthNumber Month number as per Java calendar (i.e. January is 0, February is 1 etc)
     * @return Run report for given user and month combination
     */
    @Requires(Role.ADMIN)
    @SelfAccess
    MonthlyReport getMonthlyRunReport(String actor, UserID userId, int monthNumber);

    /**
     * Fetch next page in monthly report list similar to {{@link RunService#listRuns(Actor, UserID, Filter, long, int)}
     */
    @Requires(Role.ADMIN)
    @SelfAccess
    PaginatedResult<WeeklyReport> listMonthlyReports(Actor actor, UserID userId, int offset, int limit);

}
