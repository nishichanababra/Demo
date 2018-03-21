package com.example.test.myapplication;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends ActionBarActivity {
    Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //http://192.168.1.19/flatmate/api/authentication.php?action=login&email=dhruv@gmail.com&password=yug
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callws();

            }
        });
    }


    private void callws() {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("http://192.168.1.19/flatmate/api") //Setting the Root URL
                .build(); //Finally building the adapter
        RegisterAPI api = adapter.create(RegisterAPI.class);
        api.insertUser("zz@zz.com", "zz  ",
                new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        BufferedReader reader = null;

                        //An string to store output from the server
                        String output = "";
                        try {
                            //Initializing buffered reader
                            reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
                            Log.e("Outout ", "?" + response.getBody().toString());

                            //Reading the output in the string
                            output = reader.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //Displaying the output as a toast

                        //Log.e("Outout ","?"+response.getUrl()+">"+response.getStatus());
                        //Log.e("Outout ","?"+response.getBody());


                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        Log.e("Outout ", "?" + error);
                    }
                });
    }


}
