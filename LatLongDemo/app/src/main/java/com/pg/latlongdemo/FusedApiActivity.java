package com.pg.latlongdemo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FusedApiActivity extends AppCompatActivity implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = "LocationActivity";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 100;

    private GPSTracker gpsTracker;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private static final long INTERVAL = 1000 * 1;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    private Location mCurrentLocation;

    private String mLastUpdateTime;
    private Button btnShowLocation;
    private Button btnLoadAllData;
    private TextView txtAllData;
    private TextView tvLocation;
    private Button btnClearAllData;
    // private double cssLatitude = 23.018045, cssLongitude = 72.568511;   //nalli
    public static double cssLatitude = 23.0176502982971, cssLongitude = 72.5690553785136;  //23.01781   72.5692056 current

    private ArrayList<String> arrayList = new ArrayList<>();
    public static ArrayList<LocationBean> locationArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fused_api);

        checkForPermission();

        initUIControls();

        registerForListener();


        ArrayList<LocationBean> arrayList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            LocationBean locationBean = new LocationBean();
            locationBean.setStatus("adfhdsajf" + i);
            arrayList.add(locationBean);
        }

        Bundle bundle = new Bundle();
        Intent i = new Intent(this, MainActivity.class);
        bundle.putSerializable("ARRAY_LIST", arrayList);
        i.putExtras(bundle);
        startActivity(i);

    }

    private void registerForListener() {
        btnShowLocation.setOnClickListener(this);
        btnLoadAllData.setOnClickListener(this);
        tvLocation.setOnClickListener(this);
        btnClearAllData.setOnClickListener(this);
    }

    private void initUIControls() {
        tvLocation = findViewById(R.id.tvLocation);
        btnShowLocation = findViewById(R.id.btnShowLocation);
        btnLoadAllData = findViewById(R.id.btnLoadAllData);
        txtAllData = findViewById(R.id.txtAllData);
        btnClearAllData = findViewById(R.id.btnClearAllData);
    }


    /***
     * will check for location permission
     */
    private void checkForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            askForLocationPermission();
        } else {
            checkGPSStatus();
        }

    }   //end of checkForPermission


    /***
     * will check gps status
     */
    private void checkGPSStatus() {
        gpsTracker = new GPSTracker(this);
        if (!gpsTracker.canGetLocation()) {
            gpsTracker.showSettingsAlert();
        } else {
            initLocationObjects();
        }
    }  //end of checkGPSStatus


    /***
     * will init location components
     */
    private void initLocationObjects() {
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();
    }   //end of initLocationObjects

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void askForLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

        } else {
            checkGPSStatus();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnShowLocation:

                mGoogleApiClient.connect();

                updateUI();

                arrayList.add("Lat long :   " + mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude()
                        + "\n" + "Time :: " + DateFormat.getTimeInstance().format(new Date()) + "\n" +
                        "distance :: " +
                        distance(cssLatitude, cssLongitude, mCurrentLocation.getLatitude(),
                                mCurrentLocation.getLongitude()));

                double distance = distance(cssLatitude, cssLongitude, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                if (distance < 40) {
                    Toast.makeText(this, "Check in success", Toast.LENGTH_SHORT).show();
                    setDataIntoModel(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), distance, DateFormat.getTimeInstance().format(new Date()), "success");
                } else {
                    Toast.makeText(this, "You are " + distance + " meters away", Toast.LENGTH_SHORT).show();
                    setDataIntoModel(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), distance, DateFormat.getTimeInstance().format(new Date()), "unsuccess");
                }
                break;

            case R.id.btnLoadAllData:

                Toast.makeText(this, "data cleared", Toast.LENGTH_SHORT).show();
                laodData();
                break;

            case R.id.tvLocation:
                Intent i = new Intent(this, MapsActivity.class);
                startActivity(i);
                break;

            case R.id.btnClearAllData:
                arrayList.clear();
                locationArrayList.clear();
                Toast.makeText(this, "Data cleared", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    /***
     * will set all data into model >> used for show pins in google map
     * @param latitude
     * @param longitude
     * @param distance
     * @param time
     */
    private void setDataIntoModel(double latitude, double longitude, double distance, String time, String status) {
        LocationBean locationBean = new LocationBean();
        locationBean.setDistance(distance + "");
        locationBean.setLatitude(latitude);
        locationBean.setLongitude(longitude);
        locationBean.setTime(time);
        locationBean.setStatus(status);
        locationArrayList.add(locationBean);
    }   //end of setDataIntoModel


    private void laodData() {

        String data = "";
        for (int i = 0; i < arrayList.size(); i++) {
            data += arrayList.get(i) + "\n-----------------------------------------------------\n";
        }

        txtAllData.setText(data);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        checkGPSStatus();
                    }

                } else {
                    Toast.makeText(this, "Please allow us to access location", Toast.LENGTH_SHORT).show();

                }
                return;
            }

        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}