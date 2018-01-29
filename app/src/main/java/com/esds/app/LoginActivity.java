package com.esds.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.esds.app.properties.Request;
import com.esds.app.service.RestService;
import com.esds.app.service.impl.RestServiceImpl;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    EditText un, pw;
    RestService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        un = findViewById(R.id.edittext_un);
        pw = findViewById(R.id.edittext_pw);

        apiService = new RestServiceImpl();
    }

    public void doLogin(View view) {
        final String userName = un.getText().toString().trim();
        final String password = pw.getText().toString();

        try {
            HashMap<String, String> dataSet = new HashMap<>();
            dataSet.put("userName", userName);
            dataSet.put("password", password);
            String responseData = apiService.fetchLoginData("http://192.168.1.38:8080/doLoginWithMobile", dataSet, Request.POST);

            if (responseData.equals("true")) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.putExtra("userName", userName);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, "Hatalı Kullanıcı Adı veya Parola", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
