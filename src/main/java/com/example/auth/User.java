package com.example.auth;

import com.example.helper.JsonHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

public record User(int id, String name, String country, String email) {


    // make this easily available w/o having to add mustache helpers or do it in the template
    String toJson() {
       return JsonHelper.toJson(this);
    }
}
