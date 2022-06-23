package com.example.ex3.api;

import androidx.lifecycle.MutableLiveData;

import com.example.ex3.Contact;
import com.example.ex3.Message;
import com.example.ex3.MyApplication;
import com.example.ex3.R;
import com.example.ex3.loginActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactAPI {
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;
    public static List<Contact> contacts;
    public static List<Message> messages;

    public ContactAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public List<Contact> get(String user, MutableLiveData<List<Contact>> list, String token) {
        Call<List<Contact>> call = webServiceAPI.getContacts(token);
        call.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                contacts = response.body();
                list.postValue(contacts);
                List<Contact> l = loginActivity.userDao.getContacts2();
                if (contacts != null)
                    for (Contact c : contacts) {
                        if (!tempContains(l, c.contname)) {
                            loginActivity.userDao.insertContact(new Contact(c.contname, c.userId, c.name, c.server, "c"));
                        }
                    }
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
            }
        });
        return contacts;
    }

    public boolean tempContains(List<Contact> l, String name) {
        for (Contact c : l) {
            if (c.contname.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public List<Message> getMessages(String user, MutableLiveData<List<Message>> list, String token) {
        Call<List<Message>> call = webServiceAPI.getMessages(user, token);
        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                messages = response.body();
                list.setValue(messages);
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
            }
        });
        return messages;
    }

//    public boolean tempContains(List<Message> l, String name) {
//        for(Message c : l) {
//            if (c.contname == name) {
//                return true;
//            }
//        }
//        return false;
//    }

    public void addMessage(String contId, Content c, String token) {
        Call<String> call = webServiceAPI.addMessage(contId, c, token);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String t = response.body();
                System.out.println(t);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }
}
