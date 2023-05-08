package com.example.prefab;

import cloud.prefab.client.ConfigClient;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;

public class PrefabContexClearingResponseFilter implements ContainerResponseFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(PrefabContexClearingResponseFilter.class);
    private final ConfigClient configClient;

    @Inject
    PrefabContexClearingResponseFilter(ConfigClient configClient) {
        this.configClient = configClient;
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        configClient.getContextStore().clearContext();
        LOGGER.info("Cleared context");
    }
}
