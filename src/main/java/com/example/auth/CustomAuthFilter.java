package com.example.auth;

import com.google.inject.Inject;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import java.io.IOException;
import java.util.Optional;

import static com.example.auth.UserDatabase.JEFF;


@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class CustomAuthFilter implements ContainerRequestFilter {
    public static final String USER_ATTR = "user-attr";


    // this should be available via a proxy w/o the Provider but not important to fix for the example
    @Inject
    private javax.inject.Provider<HttpServletRequest> requestProvider;


    public static Optional<User> getUserFromSession(HttpSession httpSession) {
        return Optional.ofNullable((User) httpSession.getAttribute(USER_ATTR));
    }

    public static void setUserInSession(User user, HttpSession httpSession) {
        httpSession.setAttribute(USER_ATTR, user);
    }



    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        HttpSession session = requestProvider.get().getSession();
        if (getUserFromSession(session).isEmpty()) {
            setUserInSession(JEFF, session);
        }

    }
}
