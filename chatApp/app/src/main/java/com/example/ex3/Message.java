package com.example.ex3;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Message {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;
    public String content;
    public String created;
    public boolean sent;
    public String contactId;

    public Message(String content, String created, boolean sent, String contactId) {
            this.content = content;
            this.created = created;
            this.sent = sent;
            this.contactId = contactId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public boolean isSent() {
            return sent;
        }

        public void setSent(boolean sent) {
            this.sent = sent;
        }

        public String getContactId() {
            return contactId;
        }

        public void setContactId(String contactId) {
            this.contactId = contactId;
        }
    }
