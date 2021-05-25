package com.satish;

import com.satish.config.EnvironmentVariableResolver;
import com.satish.config.Config;
import com.satish.dao.impl.DBDataSource;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * Entry point for the application
 *
 * @author satish.thulva.
 **/
public class Application {
    public static final String BASE_URI = "http://localhost:8080/myapp";

    public static HttpServer startServer() throws IOException {
        EnvironmentVariableResolver resolver = new EnvironmentVariableResolver();
        Config config = resolver.getConfig();
        DBDataSource.initialise(config.getDb());

        final ResourceConfig resourceConfig = new ResourceConfig().packages("com.satish");
        resourceConfig.register(new ApplicationModule());

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), resourceConfig);
    }

    public static void main(String[] args) throws Exception {
        HttpServer server = startServer();
        Thread.currentThread().join();
        server.stop();
    }
}
