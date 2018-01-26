package com.esds.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.esds.app.service.IApiService;
import com.esds.app.service.impl.ApiService;

public class LoginActivity extends AppCompatActivity {

    EditText un, pw;
    IApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        un = findViewById(R.id.edittext_un);
        pw = findViewById(R.id.edittext_pw);

        apiService = new ApiService();
    }

    public void doLogin(View view) {
        final String username = un.getText().toString().trim();
        final String password = pw.getText().toString();

        try {
            String responseData = apiService.prepareLoginData(username, password);
            if (responseData.equals("true")) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, "Hatalı Kullanıcı Adı veya Parola", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
