package com.example.auth;

import java.util.List;
import java.util.Optional;

public class UserDatabase {



    public static final User JEFF = new User(3, "Jeff", "US", "jeff.dwyer@prefab.cloud");


    public static final List<User> ALL_USERS = List.of(new User(1, "Tony", "UK", "tony@example.com"),
            new User(2, "Joan", "FR", "joan@example.com"),
            JEFF);

    public static Optional<User> findUserByName(String userName) {
        return ALL_USERS.stream().filter(user -> user.name().toLowerCase().equals(userName.toLowerCase())).findAny();
    }
}
