package com.example.caio.upcomingmovieslist.data;

import com.google.gson.JsonObject;

/**
 * Created by Caio on 10/02/2018.
 */

public class GenreBean {
    private Integer id;
    private String name;

    public GenreBean() {}

    public GenreBean(JsonObject jsonObject) {
        id = jsonObject.has("id") ? jsonObject.get("id").getAsInt() : 0;
        name = jsonObject.has("name") ? jsonObject.get("name").getAsString() : null;
    }

    public GenreBean(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
