package com.satish.dao;

import com.satish.dao.api.ReportDAO;
import com.satish.dao.api.RunDAO;
import com.satish.dao.api.UserDAO;
import com.satish.dao.impl.DBDataSource;
import com.satish.dao.impl.ReportDAOImpl;
import com.satish.dao.impl.RunDAOImpl;
import com.satish.dao.impl.UserDAOImpl;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

/**
 * Data Access Layer HK2 Wiring Module
 *
 * @author satish.thulva.
 **/
public class DAOModule extends AbstractBinder {
    @Override
    protected void configure() {
        bindFactory(DBFactory.class).to(DBDataSource.class);
        bind(ReportDAOImpl.class).to(ReportDAO.class).in(Singleton.class);
        bind(RunDAOImpl.class).to(RunDAO.class).in(Singleton.class);
        bind(UserDAOImpl.class).to(UserDAO.class).in(Singleton.class);
    }

    private static final class DBFactory implements Factory<DBDataSource> {
        @Override
        public DBDataSource provide() {
            return DBDataSource.getInstance();
        }

        @Override
        public void dispose(DBDataSource dbDataSource) {}
    }

}
