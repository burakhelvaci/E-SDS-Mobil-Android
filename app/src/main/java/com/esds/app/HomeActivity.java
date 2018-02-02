package com.esds.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.esds.app.messenger.MessengerActivity;
import com.esds.app.visit.VisitsActivity;

public class HomeActivity extends AppCompatActivity {

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userName = getIntent().getExtras().getString("userName");
    }

    public void visits(View view) {
        Intent intent = new Intent(HomeActivity.this, VisitsActivity.class);
        intent.putExtra("userName", userName);
        startActivity(intent);
    }

    public void chat(View view) {
        Intent intent = new Intent(HomeActivity.this, MessengerActivity.class);
        intent.putExtra("userName", userName);
        startActivity(intent);
    }
}
