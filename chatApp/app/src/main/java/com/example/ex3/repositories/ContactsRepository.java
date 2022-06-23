package com.example.ex3.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ex3.AppDB;
import com.example.ex3.Contact;
import com.example.ex3.api.ContactAPI;
import com.example.ex3.loginActivity;
import com.example.ex3.userDao;
import com.example.ex3.userWithContacts;

import java.util.LinkedList;
import java.util.List;

public class ContactsRepository {
    private userDao dao;
    private ContactListData ContactListData;
    private ContactAPI api;

    public ContactsRepository() {
        dao = loginActivity.db.userDao();
        api = new ContactAPI();
        ContactListData = new ContactListData();

    }


    class ContactListData extends MutableLiveData<List<Contact>> {


        public ContactListData() {
            super();
//            if (dao.getContacts2() != null)
//                setValue(dao.getContacts2());
            api.get("Ad", this, loginActivity.token);
        }

        @Override
        protected void onActive() {
            super.onActive();

            new Thread(() ->
            {
                api.get("Ad", this, loginActivity.token);

            }).start();

        }
    }

    public LiveData<List<Contact>> getAll() {
        return ContactListData;
    }

}