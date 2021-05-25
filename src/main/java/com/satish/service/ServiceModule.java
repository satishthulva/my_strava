package com.satish.service;

import com.satish.service.api.RunService;
import com.satish.service.api.UserService;
import com.satish.service.api.WeatherService;
import com.satish.service.impl.RunServiceImpl;
import com.satish.service.impl.UserServiceImpl;
import com.satish.service.impl.WeatherServiceImpl;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

/**
 * Service Layer HK2 Wiring Module
 *
 * @author satish.thulva
 **/
public class ServiceModule extends AbstractBinder {
    @Override
    protected void configure() {
        bind(RunServiceImpl.class).to(RunService.class).in(Singleton.class);
        bind(UserServiceImpl.class).to(UserService.class).in(Singleton.class);
        bind(WeatherServiceImpl.class).to(WeatherService.class).in(Singleton.class);
    }
}
