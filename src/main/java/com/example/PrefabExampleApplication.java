package com.example;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

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
        // TODO: application initialization
    }

    @Override
    public void run(final PrefabExampleConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
