package com.example.test.myapplication;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by test on 28/12/15.
 */
public interface RegisterAPI {
    @FormUrlEncoded
    @POST("/authentication.php?action=login")
    public void insertUser(

            @Field("password") String password,
            @Field("email") String email,
            Callback<Response> callback);
}
