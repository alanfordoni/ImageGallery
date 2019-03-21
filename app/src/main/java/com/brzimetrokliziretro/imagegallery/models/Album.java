package com.brzimetrokliziretro.imagegallery.models;

public class Album {
    private String name;
    private int userId;
    private int id;

    public Album(String name, int userId, int id) {
        this.name = name;
        this.userId = userId;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
