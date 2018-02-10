package com.esds.app.messenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.esds.app.R;
import com.esds.app.enums.Request;
import com.esds.app.service.RequestService;
import com.esds.app.service.impl.RequestServiceImpl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MessageBoxActivity extends AppCompatActivity {

    private String token;
    private RequestService requestService;
    private String hostName;
    EditText messageEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_box);

        String receiver = getIntent().getExtras().getString("receiver");
        try {
            token = new JSONObject(receiver).getString("token");
        } catch (Exception e) {
            Log.e("Error", "Exception Throwed", e);
        }

        requestService = new RequestServiceImpl();
        hostName = getString(R.string.host_name);
        messageEditText = findViewById(R.id.message_in_sender);
    }

    public void SendMessage(View view) {
        try {
            HashMap<String, String> messageDataSet = new HashMap<String, String>();
            messageDataSet.put("receiver", token);
            messageDataSet.put("message", messageEditText.getText().toString());
            requestService.affect(hostName + "/api/messenger/sendmessage", messageDataSet, Request.POST);
        } catch (Exception e) {
            Log.e("Error", "Exception Throwed", e);
        }
    }
}
