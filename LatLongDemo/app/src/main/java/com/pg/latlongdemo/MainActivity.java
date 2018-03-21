package com.pg.latlongdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    private GPSTracker gpsTracker;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private double cssLatitude = 23.0178092, cssLongitude = 72.5692072;
    private Button btnGetLocation;
    private Button btnLoadAllData;
    private Button btnClearAllData;
    private TextView txtAllData;
    private ArrayList<String> arrayList = new ArrayList<>();


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGetLocation = findViewById(R.id.btnGetLocation);
        txtAllData = findViewById(R.id.txtAllData);
        btnLoadAllData = findViewById(R.id.btnLoadAllData);
        btnClearAllData = findViewById(R.id.btnClearAllData);


        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.add("Lat long " + gpsTracker.getLatitude() + "," + gpsTracker.getLongitude() + "\n" +
                        "distance :: " + distance(cssLatitude, cssLongitude, gpsTracker.getLatitude(), gpsTracker.getLongitude()));

                Toast.makeText(MainActivity.this,
                        "Lat long " + gpsTracker.getLatitude() + "," + gpsTracker.getLongitude() + "\n" +
                                "distance :: " + distance(cssLatitude, cssLongitude, gpsTracker.getLatitude(), gpsTracker.getLongitude()),
                        Toast.LENGTH_LONG).show();
            }
        });


        btnLoadAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                laodData();
            }
        });

        btnClearAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.clear();

                Intent i = new Intent(MainActivity.this, FusedApiActivity.class);
                startActivity(i);
                finish();
                /*Toast.makeText(MainActivity.this, "Data cleared", Toast.LENGTH_SHORT).show();
                laodData();*/
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            askForLocationPermission();
        } else {
            startLocationUpdates();
        }


        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            ArrayList<LocationBean> myList = (ArrayList<LocationBean>) intent.getSerializable("ARRAY_LIST");
            Log.d(TAG, "Size :: " + myList.size());

        }

    }

    private void laodData() {
        String data = "";
        for (int i = 0; i < arrayList.size(); i++) {
            data += arrayList.get(i) + "\n--------------------------------------\n";
        }

        txtAllData.setText(data);
    }


    private void askForLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }

        } else {
            startLocationUpdates();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        startLocationUpdates();
                    }

                } else {

                    Toast.makeText(this, "Please allow us to access location", Toast.LENGTH_SHORT).show();

                }
                return;
            }

        }
    }

    private void startLocationUpdates() {


        gpsTracker = new GPSTracker(this);
        gpsTracker.getLocation();

    }


    public float distance(double lat_a, double lng_a, double lat_b, double lng_b) {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b - lat_a);
        double lngDiff = Math.toRadians(lng_b - lng_a);
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff / 2) * Math.sin(lngDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return new Float(distance * meterConversion).floatValue();
    }
}


//https://javapapers.com/android/android-location-fused-provider/ fused api