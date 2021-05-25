package com.satish.dao.impl;

import com.satish.dao.api.ReportDAO;
import com.satish.datamodels.WeeklyReport;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import javax.inject.Inject;

/**
 * @author satish.thulva.
 **/
@Slf4j
public class ReportDAOImpl extends AbstractDAO implements ReportDAO {
    @Inject
    public ReportDAOImpl(DBDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public WeeklyReport getWeeklyReport() {
        return jdbi.withExtension(ReportDAOJdbi.class, ReportDAOJdbi::getReport);
    }

    private interface ReportDAOJdbi {
        @SqlQuery("SELECT * FROM weekly_run_reports LIMIT 1")
        WeeklyReport getReport();
    }

}
