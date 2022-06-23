package com.example.ex3.api;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SendApi {
    @POST("contacts/transfer")
    Call<String> transfer(@Body Api_Transfer transfer);

    @POST("contacts/invitations")
    Call<String> invitations(@Body Api_Invite invite);
}
