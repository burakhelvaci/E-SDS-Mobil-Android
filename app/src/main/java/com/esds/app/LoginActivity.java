package com.esds.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.esds.app.enums.Request;
import com.esds.app.messenger.MyFirebaseInstanceIDService;
import com.esds.app.service.RequestService;
import com.esds.app.service.impl.RequestServiceImpl;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private String hostName;

    private EditText userNameEditText;
    private EditText passwordEditText;
    private RequestService requestService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        hostName = getString(R.string.host_name);
        userNameEditText = findViewById(R.id.user_name_in_login);
        passwordEditText = findViewById(R.id.password_in_login);

        requestService = new RequestServiceImpl();
    }

    public void doLogin(View view) {
        final String userName = userNameEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString();

        try {
            HashMap<String, String> userDataSet = new HashMap<>();
            userDataSet.put("userName", userName);
            userDataSet.put("password", password);

            String responseData = requestService.fetchLoginData(hostName + "/api/login/dologin", userDataSet, Request.POST);

            JSONObject userJSONObject = new JSONArray(responseData).getJSONObject(0);

            if (userJSONObject.getString("userName").equals(userName) && userJSONObject.getString("password").equals(password)) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.putExtra("user", userJSONObject.toString());

                MyFirebaseInstanceIDService.personnelJSONObject = userJSONObject;
                MyFirebaseInstanceIDService.hostName = hostName;

                startActivity(intent);
            } else{
                Toast.makeText(LoginActivity.this, "Hatalı Kullanıcı Adı veya Parola", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e("Error", "Exception Throwed", e);
        }
    }
}
