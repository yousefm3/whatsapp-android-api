package com.example.ex3;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ContactWithMessages {
    @Embedded
    public Contact contact;
    @Relation(
            parentColumn = "userId",
            entityColumn = "contactId"
    )
    public List<Message> messages;
}
