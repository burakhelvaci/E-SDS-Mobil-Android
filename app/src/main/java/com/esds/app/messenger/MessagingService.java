package com.esds.app.messenger;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Iterator;
import java.util.Map;

public class MessagingService extends FirebaseMessagingService {
    void print(Map<String, String> payload)
    {
        Iterator<String> it = payload.keySet().iterator();

        Log.e("x","Mesaj Geldi : ");
        while (it.hasNext())
        {
            String key = it.next();
            String val = payload.get(key);

            Log.e("x",String.format("%-10s ---> %s", key, val));
        }
    }
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        String kimden = remoteMessage.getFrom();
        Log.e("x", kimden);

        Map<String, String> payload = remoteMessage.getData();

        print(payload);

        String op = payload.get("op");

        if (op.equals("esds"))
        {
            String adr = payload.get("adr");

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(adr));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        if (op.equals("call"))
        {
            String adr = "tel:"+payload.get("adr");

            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(adr));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
