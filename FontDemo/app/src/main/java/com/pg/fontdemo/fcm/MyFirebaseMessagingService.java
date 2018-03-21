package com.pg.fontdemo.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pg.fontdemo.MainActivity;
import com.pg.fontdemo.R;

/**
 * Created by test on 21/8/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private String TAG = "MyFireBaseMessagingService";
    private long[] pattern;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "onMessageReceived remoteMessage :: " + remoteMessage);

/*
        Map<String, String> data = remoteMessage.getData();

        //you can get your text message here.
        String text = data.get("title");
        Log.d(TAG, "Text :: " + text);*/
        // sendNotification(remoteMessage.getData().get("title"),remoteMessage.getData().get("body"));
        sendNotification(remoteMessage);

    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();

        Log.d(TAG, "Messages are deleted");
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);

        Log.d(TAG, "OnMeesag");
    }


    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);

        e.printStackTrace();
    }

    private void sendNotification(RemoteMessage message) {
        Log.d(TAG, "Data Payload: " + message.getData().toString());

        String messageTitle = message.getNotification().getTitle();
        String messageBody = message.getNotification().getBody();
        String subTitle = message.getNotification().getSound();
        String tickerText = message.getNotification().getTag();

        Log.d(TAG, "messageTitle " + messageTitle + " messageBody :: " + messageBody);

        pattern = new long[]{1000, 1000, 1000, 1000, 1000};
/*        if (vibration.equalsIgnoreCase("1")) {
            pattern = new long[]{1000, 1000, 1000, 1000, 1000};
        } else {
            pattern = new long[]{000, 0, 000, 000, 000};
        }*/
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0
                //* request code *//*
                , intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)

                //.setSubText(subTitle)
                .setSubText(messageBody)
                .setVibrate(pattern)
                .setContentTitle(tickerText)
                .setLights(Color.BLUE, 1, 1)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //notificationManager.notify(0 ,*//* ID of notification *//*, notificationBuilder.build());
        notificationManager.notify(0, notificationBuilder.build());
    }
}
//http://portal.bodycarpenters.com/send_push_notification_demo.php?title=test&body=testbody