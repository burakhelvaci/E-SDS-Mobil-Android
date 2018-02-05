package com.esds.app.messenger;

import android.util.Log;

import com.esds.app.R;
import com.esds.app.enums.Request;
import com.esds.app.service.RequestService;
import com.esds.app.service.impl.RequestServiceImpl;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private RequestService requestService = new RequestServiceImpl();

    public static JSONObject personnelJSONObject;
    public static String hostName;

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic("esds");
        try {
            HashMap<String, String> personnelDataSet = new HashMap<>();
            personnelDataSet.put("id", personnelJSONObject.getString("id"));
            personnelDataSet.put("name", personnelJSONObject.getString("name"));
            personnelDataSet.put("userName", personnelJSONObject.getString("userName"));
            personnelDataSet.put("email", personnelJSONObject.getString("email"));
            personnelDataSet.put("phoneNumber", personnelJSONObject.getString("phoneNumber"));
            personnelDataSet.put("password", personnelJSONObject.getString("password"));
            personnelDataSet.put("token", refreshedToken);
            requestService.affect(hostName + "/api/token/settoken", personnelDataSet, Request.POST);
        } catch (Exception e) {
            Log.e("err", e.toString());
        }
    }
}
