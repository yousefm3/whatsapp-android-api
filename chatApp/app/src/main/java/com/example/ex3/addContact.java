package com.example.ex3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ex3.api.Api_Invite;
import com.example.ex3.api.ContactTemp;
import com.example.ex3.api.TransferApi;
import com.example.ex3.api.UserAPI;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class addContact extends AppCompatActivity {
    private AppDB db;
    private userDao userDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        db = loginActivity.db;
        userDao = loginActivity.userDao;

        Bundle extras = getIntent().getExtras();
        String username = extras.getString("username");

        Button btnSave = findViewById(R.id.saveBtn);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etItem = findViewById(R.id.etItem);
                EditText server2 = findViewById(R.id.server);
                EditText display = findViewById(R.id.display);
                //autuGenerate id + friend username
                String contactName = etItem.getText().toString();
                String server = server2.getText().toString();
                String displayName = display.getText().toString();
                userDao contactDao = null;
                AppDB db2 = Room.databaseBuilder(getApplicationContext(), AppDB.class, "appDB")
                        .allowMainThreadQueries().build();
                userDao userDao2 = db2.userDao();
                if (!contactName.equals("")) {
                    AppDB db_contact = Room.databaseBuilder(getApplicationContext(), AppDB.class, contactName)
                            .allowMainThreadQueries().build();
                    contactDao = db_contact.userDao();
                }
                if (userDao2.getUser(contactName)!=null || true) {
                    if (userDao.getContact(contactName) == null) {
//                        contactDao.insertContact(new Contact(loginActivity.userName,contactName,
//                                userDao.getUser(loginActivity.userName).getName(),"A",userDao2.getUser(contactName).getImage()));

                        TransferApi t = new TransferApi("http://" + server + "/api/");
                        t.invite(loginActivity.userName,contactName,"10.0.2.2:7008/");

                        Contact con = new Contact(contactName, username,
                                displayName, server, null);
                        userDao.insertContact(con);
                        ContactTemp cont = new ContactTemp(contactName,displayName,server);
                        Call<String> call = UserAPI.getInstance().getApi().addContact(cont,loginActivity.token);
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String resp = response.body();
                                if (response.isSuccessful()){

                                }
                                else{
                                }
                            }
                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                            }
                        });


                        Toast.makeText(getApplicationContext(),"Add succeed",Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        etItem.requestFocus();
                        etItem.setError("contact already exist");
                    }
                }else if (contactName.equals("")){
                    etItem.requestFocus();
                    etItem.setError("Field cannot be empty");
                }
                else {
                    etItem.requestFocus();
                    etItem.setError("user not found");
                }
            }
        });
        Button cancel = findViewById(R.id.cancel_add_contact);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(addContact.this, contactsList.class);
                startActivity(i);
            }
        });
    }
}