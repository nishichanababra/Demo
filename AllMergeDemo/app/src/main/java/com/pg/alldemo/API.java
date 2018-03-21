package com.pg.alldemo;

import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by test on 27/2/18.
 */

public interface API {

    final String BASE_URL = "http://portal.bodycarpenters.com/api/";

    @GET("get_leaderboard_1.php?")
    Call<JsonObject> getAllData(@QueryMap Map<String, String> params);  //
}
