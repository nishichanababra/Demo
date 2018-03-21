package com.pg.fontdemo.fcm;


import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by test on 21/8/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private String TAG = "MyFireBaseInstanceIDService";


    // 1
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.d(TAG, "Refreshed token: " + refreshedToken);


        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }


    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.

        Log.d(TAG, "Refreshed token: " + token);
    }
}
