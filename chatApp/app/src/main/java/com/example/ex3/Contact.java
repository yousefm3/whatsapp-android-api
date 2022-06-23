package com.example.ex3;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Contact {
    @NonNull
    @PrimaryKey
    public String contname;
    public String userId;
    public String name;
    public String server;
    public String imageID;

    public Contact(String contname, String userId, String name, String server, String imageID) {
        this.contname = contname;
        this.userId = userId;
        this.name = name;
        this.server = server;
        this.imageID = imageID;
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public String getContactUsername() {
        return contname;
    }

    public void setContactUsername(String contactUsername) {
        this.contname = contactUsername;
    }

    public String getCreatorUsername() {
        return userId;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.userId = creatorUsername;
    }

    public String getContactDisplayName() {
        return name;
    }

    public void setContactDisplayName(String contactDisplayName) {
        this.name = contactDisplayName;
    }

    public String getContactServer() {
        return server;
    }

    public void setContactServer(String contactServer) {
        this.server = contactServer;
    }
}


