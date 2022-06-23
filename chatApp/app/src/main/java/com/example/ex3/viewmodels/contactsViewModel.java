package com.example.ex3.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ex3.Contact;
import com.example.ex3.repositories.ContactsRepository;

import java.util.List;

public class contactsViewModel extends ViewModel {

    private ContactsRepository repository;

    private LiveData<List<Contact>> contacts;

    public contactsViewModel () {
        repository = new ContactsRepository();
        contacts = repository.getAll();
    }

    public LiveData<List<Contact>> get() { return contacts; }

    //public void add(Contact contact) { repository.add(contact); }

    //public void delete(Contact contact) { repository.delete(contact); }

    //public void reload() { repository.reload(); }
}