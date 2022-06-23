package com.example.ex3;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class user {
    @NonNull
    @PrimaryKey
    private String username;
    private String name;
    private String password;
    private String image;
    private String server;
    private String token;

    public user(@NonNull String username, String name, String password, String image, String server, String token) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.image = image;
        this.server = server;
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pass) {
        this.password = pass;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String displayName) {
        this.name = displayName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getServer() {
        return server;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setServer(String server) {
        this.server = server;
    }
}
