package com.satish;

import com.satish.accesscontrol.InterceptionServiceImpl;
import com.satish.dao.DAOModule;
import com.satish.service.ServiceModule;
import com.satish.rest.v1.TokenVerifier;
import com.satish.web.CookieVerifier;
import lombok.AllArgsConstructor;
import org.glassfish.hk2.api.InterceptionService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;
import javax.ws.rs.ext.Provider;

/**
 * All application level dependency injection wiring
 *
 * @author satish.thulva.
 **/
@Provider
@AllArgsConstructor
public class ApplicationModule extends AbstractBinder {
    @Override
    protected void configure() {
        install(new DAOModule());
        install(new ServiceModule());

        bindAsContract(CookieVerifier.class).in(Singleton.class);
        bindAsContract(TokenVerifier.class).in(Singleton.class);

        bind(InterceptionServiceImpl.class).to(InterceptionService.class).in(Singleton.class);
    }
}