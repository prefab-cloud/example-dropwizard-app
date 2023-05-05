package com.example.resources;

import com.example.auth.CustomAuthFilter;
import com.example.auth.UserDatabase;
import com.example.views.HomeView;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.TEXT_HTML)
@Path("/")
public class HomeResource {

    @GET
    public HomeView home(@Context HttpServletRequest request){
        return new HomeView()
                .setCurrentUser(CustomAuthFilter.getUserFromSession(request.getSession()).orElse(UserDatabase.JEFF))
                .setAllUsers(UserDatabase.ALL_USERS);
    }

}
