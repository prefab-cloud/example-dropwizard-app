package com.example;

import com.example.auth.CustomAuthFilter;
import com.example.config.PrefabModule;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.eclipse.jetty.server.session.SessionHandler;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import ru.vyarus.dropwizard.guice.GuiceBundle;

import java.util.Map;

public class PrefabExampleApplication extends Application<PrefabExampleConfiguration> {

    public static void main(final String[] args) throws Exception {
        new PrefabExampleApplication().run(args);
    }

    @Override
    public String getName() {
        return "PrefabExample";
    }

    @Override
    public void initialize(final Bootstrap<PrefabExampleConfiguration> bootstrap) {
        bootstrap.addBundle(new GuiceBundle.Builder().enableAutoConfig().modules(new PrefabModule()).build());
        bootstrap.addBundle(new AssetsBundle());

        bootstrap.addBundle(new ViewBundle<>() {
            @Override
            public Map<String, Map<String, String>> getViewConfiguration(PrefabExampleConfiguration configuration) {
                return configuration.getViewRendererConfiguration();
            }
        });

    }

    @Override
    public void run(final PrefabExampleConfiguration configuration,
                    final Environment environment) {
        environment.servlets().setSessionHandler(new SessionHandler());
    }

}
