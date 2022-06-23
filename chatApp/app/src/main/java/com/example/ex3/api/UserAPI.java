package com.example.ex3.api;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.ex3.Contact;
import com.example.ex3.MyApplication;
import com.example.ex3.R;
import com.example.ex3.loginActivity;
import com.example.ex3.user;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAPI {
    private static Retrofit retrofit;
    WebServiceAPI webServiceAPI;
    private static UserAPI retrofitClient;
    public static String Base_Url = "http://10.0.2.2:7008/api/";

    public static String getBase_Url() {
        return Base_Url;
    }

    public static void setBase_Url(String base_Url) {
        Base_Url = base_Url;
    }

    public UserAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(getBase_Url())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void get() {
        Call<List<user>> call = webServiceAPI.getUsers();
        call.enqueue(new Callback<List<user>>() {
            @Override
            public void onResponse(Call<List<user>> call, Response<List<user>> response) {
                List<user> users = response.body();
            }

            @Override
            public void onFailure(Call<List<user>> call, Throwable t) {
            }
        });
    }

    public static synchronized UserAPI getInstance() {
        if (retrofitClient == null) {
            retrofitClient = new UserAPI();
        }
        return retrofitClient;
    }

    public WebServiceAPI getApi() {
        return retrofit.create(WebServiceAPI.class);
    }


}
