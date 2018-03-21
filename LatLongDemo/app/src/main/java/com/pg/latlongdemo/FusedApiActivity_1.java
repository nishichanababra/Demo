package com.pg.latlongdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FusedApiActivity_1 extends AppCompatActivity implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = "LocationActivity";

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    //works fine
    /*private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;*/
    private static final long INTERVAL = 1000 * 1;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    private TextView tvLocation;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private Button btnLoadAllData;
    private Button btnClearAllData;
    private Button btnShowLocation;
    private Button btnStart;
    private ArrayList<String> arrayList = new ArrayList<>();

    private double cssLatitude = 23.018045, cssLongitude = 72.568511;

    private EditText edtLoaction;
    private TextView txtAllData;
    private boolean isStart;


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fused_api);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            askForLocationPermission();
        } else {
            startLocationUpdates();
        }

        initObjects();

        initUIControls();

        registerForListener();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            askForLocationPermission();
        } else {
            startLocationUpdates();
        }

        addDataInPreference("");

        Log.d(TAG, "Data fom preference :: " + getDataFromPreference());

    }


    private void addDataInPreference(String data) {

        SharedPreferences.Editor editor = getSharedPreferences("LAT_LONG", MODE_PRIVATE).edit();
        editor.putString("data", getDataFromPreference() + "\n" + data + "");
        editor.apply();
    }


    private String getDataFromPreference() {
        SharedPreferences prefs = getSharedPreferences("LAT_LONG", MODE_PRIVATE);
        String restoredText = prefs.getString("data", null);
        if (restoredText != null) {
            String name = prefs.getString("data", "No name defined");//"No name defined" is the default value.
            return name;
        }
        return "";
    }

    private void initObjects() {
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();
    }


    private void registerForListener() {
        btnShowLocation.setOnClickListener(this);
        btnLoadAllData.setOnClickListener(this);
        btnClearAllData.setOnClickListener(this);
        btnStart.setOnClickListener(this);

    }


    private void initUIControls() {
        btnLoadAllData = findViewById(R.id.btnLoadAllData);
        btnClearAllData = findViewById(R.id.btnClearAllData);
        txtAllData = findViewById(R.id.txtAllData);
        tvLocation = findViewById(R.id.tvLocation);
        btnShowLocation = findViewById(R.id.btnShowLocation);
        edtLoaction = findViewById(R.id.edtLoaction);
        btnStart = findViewById(R.id.btnStart);


    }


    private void laodData() {
        String data = "";
        for (int i = 0; i < arrayList.size(); i++) {
            data += arrayList.get(i) + "\n-----------------------------------------------------\n";
        }

        txtAllData.setText(data);
    }


    private void askForLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }

        } else {

        }
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart fired ..............");
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop fired ..............");
        mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    private void updateUI() {
        Log.d(TAG, "UI update initiated .............");
        if (null != mCurrentLocation) {
            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());
            tvLocation.setText("At Time: " + mLastUpdateTime + "\n" + "Latitude: " + lat + "\n" + "Longitude: " + lng + "\n" +
                    "Accuracy: " + mCurrentLocation.getAccuracy() + "Distance " + distance(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), cssLatitude, cssLongitude));
        } else {
            Log.d(TAG, "location is null ...............");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnShowLocation:
                mGoogleApiClient.connect();
                updateUI();

                if (!edtLoaction.getText().toString().isEmpty()) {
                    arrayList.add("Lat long :   " + mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude() + "\n" +
                            "Location :: " + edtLoaction.getText().toString() + "\n" +
                            "Time :: " + DateFormat.getTimeInstance().format(new Date()) + "\n" +
                            "distance :: " +
                            distance(cssLatitude, cssLongitude, mCurrentLocation.getLatitude(),
                                    mCurrentLocation.getLongitude()));

                } else {

                    arrayList.add("Lat long :   " + mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude() + "\n" +
                            "Location :: " + edtLoaction.getText().toString() + "\n" +
                            "Time :: " + DateFormat.getTimeInstance().format(new Date()) + "\n" +
                            "distance :: " +
                            distance(cssLatitude, cssLongitude, mCurrentLocation.getLatitude(),
                                    mCurrentLocation.getLongitude()));


                }


                edtLoaction.setText("");
                Toast.makeText(FusedApiActivity_1.this,
                        "Lat long " + mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude() + "\n" +
                                "distance :: " + distance(cssLatitude, cssLongitude, mCurrentLocation.getLatitude(),
                                mCurrentLocation.getLongitude()),
                        Toast.LENGTH_LONG).show();
                break;

            case R.id.btnLoadAllData:
                laodData();
                break;

            case R.id.btnClearAllData:

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:

                                addDataInPreference(arrayList.toString());
                                Toast.makeText(FusedApiActivity_1.this, "Data cleared", Toast.LENGTH_SHORT).show();
                                arrayList.clear();
                                laodData();


                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to clear all data ?\nYour data will still remain in preference").
                        setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

                break;

            case R.id.btnStart:
                if (isStart) {
                    arrayList.add("===============================\n" + "End at :: " + edtLoaction.getText().toString()
                            + " AT " + mLastUpdateTime + "\n===============================");
                    btnStart.setText("START");
                    isStart = false;
                } else {
                    arrayList.add("===============================\n" + "Start From :: " + edtLoaction.getText().toString() +
                            " AT " + mLastUpdateTime + "\n===============================");
                    isStart = true;
                    btnStart.setText("STOP");
                }

                break;


        }
    }
}