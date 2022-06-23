package com.example.ex3.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


import com.example.ex3.Message;
import com.example.ex3.repositories.ChatRepository;
import com.example.ex3.repositories.ContactsRepository;

import java.util.List;

public class ChatViewModel extends ViewModel {

    public ChatRepository repository;

    private LiveData<List<Message>> chat;


    public ChatViewModel (String contactName, String image) {
        repository = new ChatRepository(contactName, image);
        chat = repository.getAll();
    }

    public LiveData<List<Message>> get() { return chat; }

    //public void add(Contact contact) { repository.add(contact); }

    //public void delete(Contact contact) { repository.delete(contact); }

    //public void reload() { repository.reload(); }
}