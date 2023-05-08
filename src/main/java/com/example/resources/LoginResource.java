package com.example.resources;


import com.example.auth.CustomAuthFilter;
import com.example.auth.UserDatabase;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/login")
public class LoginResource {
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response loginAs(@Context HttpServletRequest request, @FormParam("username") String userName) {
        request.getSession().setAttribute(CustomAuthFilter.USER_ATTR, UserDatabase.findUserByName(userName).orElse(UserDatabase.JEFF));
        return Response.seeOther(URI.create("/")).build();
    }
}
