package com.example.ex3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ex3.api.UserAPI;

import java.io.FileNotFoundException;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class registerActivity extends AppCompatActivity {
    private static final int SELECT_IMAGE_CODE = 1;
    public static String uri = null;
    EditText usernameIt, displayNameIt, passIt, passConfirmationIt;
    private AppDB db;
    private userDao userDao;
    private MutableLiveData<String> temp;
    private String userName;
    private String displayName;
    private String pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        temp = new MutableLiveData<>();
        usernameIt = findViewById(R.id.id_username);
        displayNameIt = findViewById(R.id.id_displayName);
        passIt = findViewById(R.id.id_password);
        passConfirmationIt = findViewById(R.id.id_passConfirmation);
        Button registerBtn = findViewById(R.id.registerBtn);
        //sign in button
        TextView btn = findViewById(R.id.signinlink);
        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "appDB")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        userDao = db.userDao();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(registerActivity.this, loginActivity.class);
                startActivity(intent);
            }
        });

        temp.observe(this, v -> {
            user u = new user(userName, displayName, pass,uri, "server","token");
            AppDB db2 = Room.databaseBuilder(getApplicationContext(), AppDB.class, userName)
                    .allowMainThreadQueries().build();
            userDao userDao2 = db2.userDao();
            userDao2.insertUser(u);

            Intent intent = new Intent(this, loginActivity.class);
            startActivity(intent);
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = usernameIt.getText().toString();
                displayName = displayNameIt.getText().toString();
                pass = passIt.getText().toString();
                String passConfirmation = passConfirmationIt.getText().toString();
                registerUser(userName,displayName,pass);
//
//                boolean check = validateInfo(userName, displayName, pass, passConfirmation);
//                if (check){
//                    registerUser(userName, displayName, pass);
//                    user u = new user(userName, displayName, pass,uri, "server","token");
//                    userDao.insertUser(u);
//                    Toast.makeText(getApplicationContext(),"Registeration succeed",Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(registerActivity.this, loginActivity.class);
//                    AppDB db2 = Room.databaseBuilder(getApplicationContext(), AppDB.class, userName)
//                            .allowMainThreadQueries().build();
//                    userDao userDao2 = db2.userDao();
//                    userDao2.insertUser(u);
//                    startActivity(intent);
//                    finish();
//                } else{
//                    Toast.makeText(getApplicationContext(),"Registeration failed",Toast.LENGTH_SHORT).show();
//                }

            }
        });
        ///add image button
        Button choose = findViewById(R.id.AddImageBtn);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(Intent.createChooser(intent, "title"),SELECT_IMAGE_CODE);
            }
        });
    }
    private void registerUser(String username, String name, String password) {
        user u = new user(username,name, password, uri,"server","token");
        Call<String> call = UserAPI.getInstance().getApi().register(u);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String responseRegister = response.body();
                temp.setValue(responseRegister);
                if (response.isSuccessful()){
                    Toast.makeText(registerActivity.this,"API succeed register", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(registerActivity.this,String.valueOf(responseRegister), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(registerActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean validateInfo(String userName, String displayName, String pass, String passConfirmation) {
        if (userName.length() == 0){
            usernameIt.requestFocus();
            usernameIt.setError("Field cannot be empty");
            return false;
        } else if(displayName.length() == 0){
            displayNameIt.requestFocus();
            displayNameIt.setError("Field cannot be empty");
            return false;
        } else if (userDao.getUser(userName)!=null) {
            usernameIt.requestFocus();
            usernameIt.setError("username exist");
            return false;
        }
        else if(pass.length() == 0){
            passIt.requestFocus();
            passIt.setError("Field cannot be empty");
            return false;
        }
        else if(passConfirmation.length() == 0){
            passConfirmationIt.requestFocus();
            passConfirmationIt.setError("Field cannot be empty");
            return false;
        }else if(userName.length() < 4){
            usernameIt.requestFocus();
            usernameIt.setError("Username length must be at least 4");
            return false;
        }else if(displayNameIt.length() < 4){
            displayNameIt.requestFocus();
            displayNameIt.setError("display name length must be at least 4");
            return false;
        }
        else if(!pass.matches(".{8,20}")){
            passIt.requestFocus();
            passIt.setError("Password length must be at least 8 and maximum 20");
            return false;
        }else if(!pass.matches(".*\\d+.*")){
            passIt.requestFocus();
            passIt.setError("Password must contain at least one digit");
            return false;
        }else if(!containLowerCase(pass)){
            passIt.requestFocus();
            passIt.setError("Password must contain at least one lower case English letter");
            return false;
        }else if(!containUpperCase(pass)){
            passIt.requestFocus();
            passIt.setError("Password must contain at least one upper case English letter");
            return false;
        }else if(!pass.equals(passConfirmation)){
            passConfirmationIt.requestFocus();
            passConfirmationIt.setError("Password confirmation don't match password");
            return false;
        }
        return true;
    }
    boolean containLowerCase(String str) {
        boolean lowerCaseFlag = false;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (Character.isLowerCase(ch)) {
                lowerCaseFlag = true;
            }
        }
        if (lowerCaseFlag) {
            return true;
        }
        return false;
    }
    boolean containUpperCase(String str) {
        boolean upperCaseFlag = false;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (Character.isUpperCase(ch)) {
                upperCaseFlag = true;
            }
        }
        if (upperCaseFlag) {
            return true;
        }
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1) {
                if (data != null) {
                    uri = data.getData().toString();
                    Toast.makeText(registerActivity.this,"Picture Uploaded Successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(registerActivity.this,"Done without uploading picture", Toast.LENGTH_SHORT).show();
                }
        }
    }
}