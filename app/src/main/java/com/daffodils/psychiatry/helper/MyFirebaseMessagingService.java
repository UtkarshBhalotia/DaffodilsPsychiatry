package com.daffodils.psychiatry.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.daffodils.psychiatry.R;
import com.daffodils.psychiatry.activity.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String title, message;
    int badgeCount;
    private static final String TAG = "MyFirebaseMsgService";
    public static final String mypreference = "mypref";
    SharedPreferences sharedPreferences;
    private Context context;
    Intent intent;
    String msgid, token_id;
    NotificationManager notificationManager;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        token_id = s;
        Log.d("NEW_TOKEN",s);
    }

    public String getTokenId(){
        return token_id;
    }

    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        //Calling method to generate notifiction
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        //sharedPreferences = getSharedPreferences(GlobalConst.MyPREFERENCES, Context.MODE_PRIVATE);
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            title = remoteMessage.getData().get("title");
            message = remoteMessage.getData().get("body");
            sendNotification(message);

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            title = remoteMessage.getNotification().getTitle().trim();
            message = remoteMessage.getNotification().getBody().trim();
            msgid = remoteMessage.getMessageId();
          /*  SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy HH:mm");
            String currentDateandTime = sdf.format(new Date());
            insertnotificationintodb(title, msgid, "false", message, currentDateandTime);*/
            sendNotification(message);

        }


    }

    private void sendNotification(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationChannel mChannel = new NotificationChannel(title, message, NotificationManager.IMPORTANCE_HIGH);

            Notification notification = new Notification.Builder(MyFirebaseMessagingService.this)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.logo)
                    .setChannelId(title)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)

                    .setContentIntent(pendingIntent)
                    .build();
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(mChannel);

            int notifyID = 1;
            mNotificationManager.notify(notifyID, notification);
        }
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)

                .setSmallIcon(R.drawable.logo)
                //.setLargeIcon(bitmap)
                .setContentText(message)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());


    }
}