package com.example.resources;

import com.example.auth.CustomAuthFilter;
import com.example.auth.UserDatabase;
import com.example.views.HomeView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.TEXT_HTML)
@Path("/")
public class HomeResource {
    private static final Logger LOG = LoggerFactory.getLogger(HomeResource.class);

    @GET
    public HomeView home(@Context HttpServletRequest request){
        LOG.debug("🔍 Hello debug logger");
        LOG.info("ℹ️ Hello info logger");
        LOG.warn("⚠️ Hello warn logger");
        LOG.error("🚨 Hello error logger");


        return new HomeView()
                .setCurrentUser(CustomAuthFilter.getUserFromSession(request.getSession()).orElse(UserDatabase.JEFF))
                .setAllUsers(UserDatabase.ALL_USERS);
    }

}
