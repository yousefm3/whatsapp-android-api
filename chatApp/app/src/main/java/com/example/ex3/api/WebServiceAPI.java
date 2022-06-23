package com.example.ex3.api;

import com.example.ex3.Contact;
import com.example.ex3.Message;
import com.example.ex3.user;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebServiceAPI {
 @POST("contacts/register")
 Call<String> register(@Body user u);

 @POST("contacts/login")
 Call<String> login(@Body user u);

 @GET("contacts")
 Call<List<Contact>> getContacts(@Header("Authorization") String token);

 @GET("contacts/users")
 Call<List<user>> getUsers();

 @POST("contacts")
 Call<String> addContact(@Body ContactTemp cont, @Header("Authorization") String token);

 @GET("contacts/{ContID}")
 Call<Contact> getContact(@Path("ContID") String ContID, @Header("Authorization") String token);

 @GET("contacts/{ContID}/messages")
 Call<List<Message>> getMessages(@Path("ContID") String ContID, @Header("Authorization") String token);

 @POST("contacts/{ContID}/messages")
 Call<String> addMessage(@Path("ContID") String ContID, @Body Content Content, @Header("Authorization") String token);
}