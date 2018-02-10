package com.esds.app.messenger;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        MyFirebaseInstanceIDService myFirebaseInstanceIDService = new MyFirebaseInstanceIDService();
        myFirebaseInstanceIDService.onTokenRefresh();
        Log.e("x", "From: " + remoteMessage.getFrom());

        Log.e("x", "Size : " + remoteMessage.getData().size());

        showMessage(remoteMessage.getFrom(), remoteMessage.getData().get("message"));

        if (remoteMessage.getNotification() != null) {
            Log.d("Yeni Mesaj", remoteMessage.getNotification().getBody());
        }
    }

    void showMessage(String sender, String message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);

        Intent intent = new Intent(this, IncomingMessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("sender", sender);
        intent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentTitle(sender);
        notificationBuilder.setContentText(message);

        notificationBuilder.setContentIntent(pendingIntent);

        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSmallIcon(android.R.drawable.ic_menu_info_details);


        SP sp = new SP(this);
        int id = sp.getId();

        notificationManager.notify(id, notificationBuilder.build());
    }
}
