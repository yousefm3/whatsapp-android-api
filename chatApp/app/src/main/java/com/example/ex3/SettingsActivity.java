package com.example.ex3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ex3.api.UserAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

public class SettingsActivity extends AppCompatActivity {
    static Switch aSwitch;
    static String color = "#3262C4";
    Button changeBtn;
    EditText url_Input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ImageButton btnReturn = findViewById(R.id.backbuttonofspecificchat);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingsActivity.this, contactsList.class);
                startActivity(i);
                finish();
            }
        });
        aSwitch = findViewById(R.id.mode);
        TextView bar = findViewById(R.id.Bar);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override

            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(aSwitch.isChecked()) {
                   color = "#3262C4";
                } else {
                    color = "#075e54";
                }
                bar.setBackgroundColor(Color.parseColor(color));
            }
        });
        changeBtn = findViewById(R.id.changeBtn);
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url_Input = findViewById(R.id.urlInput);
                String new_url = url_Input.getText().toString();
                UserAPI.setBase_Url(new_url);
                Toast.makeText(SettingsActivity.this, "Base Url changed successfully", Toast.LENGTH_SHORT).show();
            }
        });



    }

}