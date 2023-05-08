package com.example.views;

import com.example.auth.User;
import io.dropwizard.views.View;

import java.util.List;

public class HomeView extends View {


    User currentUser;

    List<User> allUsers;

    boolean showGdprBanner;

    public HomeView(){
        super("home.mustache");
    }


    public User getCurrentUser() {
        return currentUser;
    }

    public HomeView setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        return this;
    }

    public List<User> getAllUsers() {
        return allUsers;
    }

    public HomeView setAllUsers(List<User> allUsers) {
        this.allUsers = allUsers;
        return this;
    }

    public boolean isShowGdprBanner() {
        return showGdprBanner;
    }

    public HomeView setShowGdprBanner(boolean showGdprBanner) {
        this.showGdprBanner = showGdprBanner;
        return this;
    }
}
