package com.satish.rest.v1;

import com.satish.AuthRequired;
import com.satish.dao.api.ReportDAO;
import com.satish.datamodels.WeeklyReport;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;

/**
 * REST API for reports resource
 *
 * @author satish.thulva
 */
@Slf4j
@Path("rest/v1/reports")
public class Reports extends AbstractRestEndPoint {
    @Inject
    private ReportDAO reportDAO;

    @GET
    @Path("weekly")
    @Produces(MediaType.APPLICATION_JSON)
    @AuthRequired
    public List<WeeklyReport> getReports() {
        try {
            return Collections.singletonList(reportDAO.getWeeklyReport());
        } catch (Exception e) {
            log.error("something wrong", e);
            throw e;
        }
    }

}
