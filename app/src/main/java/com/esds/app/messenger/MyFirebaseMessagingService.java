package com.esds.app.messenger;

import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Iterator;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        MyFirebaseInstanceIDService myFirebaseInstanceIDService = new MyFirebaseInstanceIDService();
        myFirebaseInstanceIDService.onTokenRefresh();
        Log.e("x", "From: " + remoteMessage.getFrom());

        Log.e("x", "Size : " + remoteMessage.getData().size());

        if (remoteMessage.getData().size() > 0) {
            Map<String,String> response = remoteMessage.getData();
            Iterator<String> iterator = response.keySet().iterator();

            while (iterator.hasNext()){
                String key = iterator.next();
                String value = response.get(key);

                Log.e("x", key + " - " + value);
            }
        }

        if (remoteMessage.getNotification() != null) {
            Log.e("x", "Notification Body : " + remoteMessage.getNotification().getBody());
        }
    }
}
