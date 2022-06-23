package com.example.ex3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ex3.adapters.chatAdapter;
import com.example.ex3.adapters.contactsListAdapter;
import com.example.ex3.api.Content;
import com.example.ex3.api.TransferApi;
import com.example.ex3.api.UserAPI;
import com.example.ex3.viewmodels.ChatViewModel;
import com.example.ex3.viewmodels.contactsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class chat extends AppCompatActivity implements RecyclerViewItem{
    userDao userDao = loginActivity.userDao;
    private List<Message> messages;
    contactsListAdapter adapter;
    private ChatViewModel viewModel;
    private contactsViewModel _view = new contactsViewModel();
    EditText usernameET;
    String userName = loginActivity.userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println(loginActivity.token);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        RecyclerView _lvItems = findViewById(R.id.lstPosts_horizontal);
        Bundle extras = getIntent().getExtras();
        String ContactId = extras.getString("contactId");
        String ImageId = extras.getString("imageId");
        String name = extras.getString("name");
        String server = extras.getString("server");
        viewModel = new ChatViewModel(ContactId, ImageId);
        chatAdapter chat_adapter;
        ImageView imageView = findViewById(R.id.imageviewofXuser);
        //Uri myUri = Uri.parse(loginActivity.usersDao2.getUser(ContactId).getImage());
        //imageView.setImageURI(myUri);
        TextView textView = findViewById(R.id.Nameofspecificuser);
        textView.setText(name);
        ImageButton btnReturn = findViewById(R.id.backbuttonofspecificchat);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(chat.this, contactsList.class);
                startActivity(i);
                finish();
            }
        });
        usernameET = findViewById(R.id.login_username);
        FloatingActionButton btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(view -> {
            Intent i = new Intent(chat.this, addContact.class);
            i.putExtra("username",userName);
            startActivity(i);
        });
        FloatingActionButton settings = findViewById(R.id.settingBtn);
        settings.setOnClickListener(view -> {
            Intent i = new Intent(chat.this, SettingsActivity.class);
            startActivity(i);
            finish();
        });
        ImageButton btnSend = findViewById(R.id.imageviewsendmessage);
        AppDB db_contact = Room.databaseBuilder(getApplicationContext(), AppDB.class, ContactId)
                .allowMainThreadQueries().build();
        userDao contactDao = db_contact.userDao();

        TransferApi t = new TransferApi("http://" + server + "/api/");

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
                EditText content = findViewById(R.id.getmessage);
                Message msgSender = new Message(content.getText().toString(),time,true,ContactId);
                Message msgReciever = new Message(content.getText().toString(),time,false,loginActivity.userName);

                Content c1 = new Content(content.getText().toString());

                t.transfer(loginActivity.userName,ContactId,content.getText().toString());

                userDao.insertMessage(msgSender);
                contactDao.insertMessage(msgReciever);
                viewModel.repository.add(content.getText().toString());
                ContactWithMessages c = userDao.getMessages(ContactId);
                content.setText("");
//                viewModel.get();
                // userDao.insertMessage(msgReviever);
            }
        });


        RecyclerView lvItems = findViewById(R.id.recyclerviewofspecific);
        chat_adapter = new chatAdapter(this,messages);
        lvItems.setAdapter(chat_adapter);
        lvItems.setLayoutManager(new LinearLayoutManager(this));
//        viewModel.get().observe(this, new Observer<List<Message>>() {
//            @Override
//            public void onChanged(List<Message> messages) {
//                chat_adapter.setMessagesArrayList(messages);
//            }
//        });
        viewModel.repository.getAll().observe(this, v -> {
            chat_adapter.setMessagesArrayList(v);
        });
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            adapter = new contactsListAdapter(this, this);
            _lvItems.setAdapter(adapter);
            _lvItems.setLayoutManager(new LinearLayoutManager(this));
            _view.get().observe(this, contacts -> {
                adapter.setContacts(contacts);
            });
        }
    }


    @Override
    public void onItemClick(int position) {
        TextView textView = findViewById(R.id.Nameofspecificuser);
        ImageView imageView = findViewById(R.id.imageviewofXuser);
        String stringImage = userDao.getContacts(loginActivity.userName).contacts.get(position).getImageID();
        textView.setText(userDao.getContacts(loginActivity.userName).contacts.get(position).getContactDisplayName());
        imageView.setImageURI(Uri.parse(stringImage));

    }
}