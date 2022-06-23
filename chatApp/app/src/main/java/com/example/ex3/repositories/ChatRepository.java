package com.example.ex3.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ex3.AppDB;
import com.example.ex3.Contact;
import com.example.ex3.ContactWithMessages;
import com.example.ex3.Message;
import com.example.ex3.api.ContactAPI;
import com.example.ex3.api.Content;
import com.example.ex3.loginActivity;
import com.example.ex3.userDao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class ChatRepository {
    private userDao dao;
    public ChatData ChatData;
    private ContactAPI api;
    private String contactName;
    private String imageId;

    public ChatRepository(String contact, String image) {
        dao = loginActivity.userDao;
        contactName = contact;
        api = new ContactAPI();
        ChatData = new ChatData();
        imageId = image;

    }


    class ChatData extends MutableLiveData<List<Message>> {


        public ChatData() {
            super();
            ContactWithMessages t = dao.getMessages(contactName);
            if(t != null)
                setValue(t.messages);
            else {
                setValue(new LinkedList<>());
            }
            setValue(api.getMessages(contactName, this, loginActivity.token));
        }

        @Override
        protected void onActive() {
            super.onActive();
//            new Thread(() ->
//            {
//                if(dao.getMessages(loginActivity.userName) != null)
//                    ChatData.postValue(dao.getMessages(loginActivity.userName).messages);
//
//            }).start();

        }
    }
    public MutableLiveData<List<Message>> getAll() {
        return ChatData;
    }
    public void add(String c) {
        Content content = new Content(c);
        api.addMessage(contactName,content,loginActivity.token);
        List<Message> l = ChatData.getValue();
        l.add(new Message(c, new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()),true,contactName ));
        ChatData.setValue(l);
    }
}