package com.example.ex3;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;


@Dao
public interface userDao {


    @Query("SELECT * FROM User WHERE username = :username")
    user getUser(String username);

    @Transaction
    @Query("SELECT * FROM User WHERE username = :username")
    userWithContacts getContacts(String username);

    @Transaction
    @Query("SELECT * FROM Contact WHERE userId = :contactUsername")
    ContactWithMessages getMessages(String contactUsername);

    @Query("SELECT * FROM Contact WHERE userId = :contactUsername")
    Contact getContact(String contactUsername);

    @Query("SELECT * FROM Contact")
    List<Contact> getContacts2();

    @Insert
    void insertUser(user... users);

    @Insert
    void insertContact(Contact... contacts);

    @Update
    void updateContact(Contact... contacts);

    @Delete
    void deleteContact(Contact... contacts);

    @Insert
    void insertMessage(Message... messages);

}