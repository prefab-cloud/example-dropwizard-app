package com.example.prefab;

import cloud.prefab.client.ConfigClient;
import cloud.prefab.context.PrefabContext;
import com.example.auth.CustomAuthFilter;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;

@Priority(Priorities.USER)
public class PrefabContextAddingRequestFilter implements ContainerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(PrefabContextAddingRequestFilter.class);
    private final Provider<HttpServletRequest> requestProvider;
    private final ConfigClient configClient;

    @Inject
    public PrefabContextAddingRequestFilter(Provider<HttpServletRequest> requestProvider, ConfigClient configClient) {
        this.requestProvider = requestProvider;
        this.configClient = configClient;
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        // we're not using real auth here so we won't be using eg `containerRequestContext.getSecurityContext()`
        CustomAuthFilter.getUserFromSession(requestProvider.get().getSession()).ifPresent(user -> {
            LOGGER.info("Adding context for {}", user.name());
            configClient.getContextStore().addContext(PrefabContext.newBuilder("User")
                    .put("name", user.name())
                    .put("id", user.id())
                    .put("email", user.email())
                    .put("country", user.country())
                    .build());
        });
    }
}
