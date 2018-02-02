package com.esds.app.messenger;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

public class RegistrationService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String fId = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic("esds");

        Log.e("x", fId);
    }
}
