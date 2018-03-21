package com.pg.latlongdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String TAG = "MapsActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng css = new LatLng(FusedApiActivity.cssLatitude, FusedApiActivity.cssLongitude);
        mMap.addMarker(new MarkerOptions().position(css).title("Marker off css").
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(css));
        Log.d(TAG, "onMapReady: " + FusedApiActivity.locationArrayList.size());

        for (int i = 0; i < FusedApiActivity.locationArrayList.size(); i++) {
            Log.d(TAG, "onMapReady : inside loop " + FusedApiActivity.locationArrayList.size());

            LocationBean locationBean = FusedApiActivity.locationArrayList.get(i);
            Log.d(TAG, "onMapReady : factch lat long " + locationBean.getLatitude() + "," + locationBean.getLongitude());
            LatLng location = new LatLng(locationBean.getLatitude(), locationBean.getLongitude());
            mMap.addMarker(new MarkerOptions().
                    position(location).title(locationBean.getTime() + " status " + locationBean.getStatus()).
                    icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        }


        CircleOptions circleOptions = new CircleOptions()
                .center(css)   //set center
                .radius(40)   //set radius in meters
                .fillColor(0x55f44f49)  //default
                .strokeColor(Color.BLUE)
                .strokeWidth(1);
        mMap.addCircle(circleOptions);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(css, 18));

    }
}
