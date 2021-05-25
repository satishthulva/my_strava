package com.satish.rest.v1;

import com.satish.AuthRequired;
import com.satish.accesscontrol.Actor;
import com.satish.datamodels.PaginatedResult;
import com.satish.datamodels.UserID;
import com.satish.dto.AddRun;
import com.satish.datamodels.LatLong;
import com.satish.datamodels.Run;
import com.satish.service.api.RunService;
import com.satish.utils.FilterParser;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * REST API for runs resource
 *
 * @author satish.thulva
 */
@Slf4j
@Path("rest/v1/runs")
public class Runs extends AbstractRestEndPoint {
    @Inject
    private RunService runService;

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @AuthRequired
    public void addRun(AddRun addRun) {
        Actor actor = getActor();
        UserID userID = new UserID(addRun.getUserId());
        runService.createRun(actor, userID, addRun.getDistanceInKm(), addRun.getDurationInHours(),
                LatLong.fromString(addRun.getLocation()), addRun.getDate(), addRun.getTimeOfTheDay());
    }

    @GET
    @Path("/user/{id}/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @AuthRequired
    public PaginatedResult<Run> listRuns(@PathParam("id") long userId,
                                         @QueryParam("filter") @DefaultValue("") String filter,
                                         @QueryParam("fromRunId") @DefaultValue("1") long fromRunId,
                                         @QueryParam("pageSize") @DefaultValue("30") int pageSize) {
        Actor actor = getActor();
        UserID userID = new UserID(userId);
        return runService.listRuns(actor, userID, FilterParser.parse(filter), fromRunId, pageSize);
    }

}
