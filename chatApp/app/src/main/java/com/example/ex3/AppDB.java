package com.example.ex3;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {user.class, Contact.class, Message.class},version = 1)
public abstract class AppDB extends RoomDatabase {
    public abstract userDao userDao();
}