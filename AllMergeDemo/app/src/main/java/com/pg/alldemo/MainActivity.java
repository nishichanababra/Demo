package com.pg.alldemo;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends ParentAcitivity {

    private String TAG = MainActivity.this.getClass().getSimpleName();
    private RecyclerView reclyer;
    private List<UserBean> userList = new ArrayList<>();
    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initUIControls();

        dataBaseHelper =
                Room.databaseBuilder(this, DataBaseHelper.class, "LeaderboardUser")
                        .allowMainThreadQueries()   //no need to add your code in aync task or handler
                        .build();


        calLWebserviceForGetData();

    }


    /***
     * will call webservice for getting data
     */
    private void calLWebserviceForGetData() {
        if (isNetworkAvailable(this)) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            API api = retrofit.create(API.class);

            HashMap<String, String> params = new HashMap<>();
            params.put("action", "get_leaderboard");
            params.put("page_number", "1");
            params.put("last_rank", "21");
            params.put("member_id", "65");

            Call<JsonObject> call = api.getAllData(params);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));
                        //Log.d(TAG, "onResponse: " + jsonObject);
                        UserBean userBean = getDataBean(jsonObject.toString());
                        Log.d(TAG, "onResponse leaderboard size: " + userBean.getData().getLeaderboard().size());

                        insertDataIntoDb(userBean.getData().getLeaderboard());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        Toast.makeText(MainActivity.this, "Time out error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "No internet connection detected", Toast.LENGTH_SHORT).show();
        }


    }   //end of calLWebserviceForGetData


    /***
     * insert data into db
     * @param leaderboard
     */
    private void insertDataIntoDb(List<UserBean.Leaderboard> leaderboard) {
        ArrayList<User> arrayList = new ArrayList<>();
        for (int i = 0; i < leaderboard.size(); i++) {
            User user = new User();
            user.setId(leaderboard.get(i).getMember_id());
            user.setName(leaderboard.get(i).getName());
            user.setPicturePath(leaderboard.get(i).getProfile_pic());
            arrayList.add(user);
        }

        dataBaseHelper.getUserData().clearAllTableData();   //clear before insert

        dataBaseHelper.getUserData().insertAllData(arrayList);  // insert all data

        Log.d(TAG, "insertDataIntoDb : " + dataBaseHelper.getUserData().getAllUserData().size());
        if (dataBaseHelper.getUserData().getAllUserData().size() > 0) {
            setAdapter();
        }

    }   //end of insertDataIntoDb


    @Override
    protected void setAdapter() {
        super.setAdapter();
        reclyer.setLayoutManager(new LinearLayoutManager(this));
        reclyer.setAdapter(new UserAdapter(this, dataBaseHelper.getUserData().getAllUserData()));
    }

    @Override
    protected void initUIControls() {
        super.initUIControls();
        reclyer = findViewById(R.id.reclyer);
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }//end of isNetworkAvailable


    /**
     * @param jsonDataObj
     * @return
     */
    public UserBean getDataBean(String jsonDataObj) {
        Gson gson = new GsonBuilder().create();
        UserBean subCategoryBean = gson.fromJson(jsonDataObj, UserBean.class);
        return subCategoryBean;
    }//end o getProductDetailBeanFromJson
}
