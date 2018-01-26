package com.esds.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.esds.app.chat.ChatActivity;
import com.esds.app.orders.OrdersActivity;
import com.esds.app.products.ProductsActivity;
import com.esds.app.visits.VisitsActivity;

public class HomeActivity extends AppCompatActivity {

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        username = getIntent().getExtras().getString("username");
    }

    public void visits(View view) {
        Intent intent = new Intent(HomeActivity.this, VisitsActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void products(View view) {
        Intent intent = new Intent(HomeActivity.this, ProductsActivity.class);
        startActivity(intent);
    }

    public void chat(View view) {
        Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
        startActivity(intent);
    }

    public void orders(View view) {
        Intent intent = new Intent(HomeActivity.this, OrdersActivity.class);
        startActivity(intent);
    }
}
