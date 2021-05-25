package com.satish.dao.impl;

import com.satish.dao.impl.jdbi.mappers.UserMapper;
import com.satish.dao.impl.jdbi.mappers.RunMapper;
import com.satish.dao.impl.jdbi.mappers.WeeklyReportMapper;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

/**
 * Abstract DAO with common Jdbi specific config done
 *
 * @author satish.thulva.
 **/
public class AbstractDAO {

    protected Jdbi jdbi;

    public AbstractDAO(DBDataSource dbDataSource) {
        Jdbi jdbi = Jdbi.create(dbDataSource.getDataSource());
        jdbi.installPlugin(new SqlObjectPlugin());

        jdbi.registerRowMapper(new WeeklyReportMapper());
        jdbi.registerRowMapper(new UserMapper());
        jdbi.registerRowMapper(new RunMapper());

        this.jdbi = jdbi;
    }

}
