package com.esds.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.esds.app.messenger.MessengerActivity;
import com.esds.app.visit.VisitsActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {

    private JSONObject userJSONObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        try {
            userJSONObject = new JSONObject(getIntent().getExtras().getString("user"));
        } catch (Exception e) {
            Log.e("Error", "Exception Throwed", e);
        }
    }

    public void visits(View view) {
        Intent intent = new Intent(HomeActivity.this, VisitsActivity.class);
        intent.putExtra("user", userJSONObject.toString());
        startActivity(intent);
    }

    public void chat(View view) {
        Intent intent = new Intent(HomeActivity.this, MessengerActivity.class);
        intent.putExtra("user", userJSONObject.toString());
        startActivity(intent);
    }
}
