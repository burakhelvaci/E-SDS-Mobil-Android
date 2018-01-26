package com.esds.app.visits;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.esds.app.R;

public class VisitDetailActivity extends AppCompatActivity {

    TextView cName;
    TextView cPhoneNumber;
    TextView cAddress;
    TextView vDate;

    String customerName;
    String customerPhoneNumber;
    String customerLocation;
    String customerAddres;
    String visitDate;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_detail);

        customerName = getIntent().getExtras().getString("customerName");
        customerPhoneNumber = getIntent().getExtras().getString("customerPhoneNumber");
        customerLocation = getIntent().getExtras().getString("customerLocation");
        customerAddres = getIntent().getExtras().getString("customerAddres");
        visitDate = getIntent().getExtras().getString("visitDate");
        id = getIntent().getExtras().getString("id");

        cName = findViewById(R.id.customer_detail_name);
        cPhoneNumber = findViewById(R.id.customer_detail_phonenumber);
        cAddress = findViewById(R.id.customer_detail_address);
        vDate = findViewById(R.id.visit_detail_date);

        cName.setText(customerName);
        cPhoneNumber.setText(customerPhoneNumber);
        cAddress.setText(customerAddres);
        vDate.setText(visitDate);
    }

    public void getLocation(View view) {
        Intent intent = new Intent(VisitDetailActivity.this, MapActivity.class);
        intent.putExtra("customerName", customerName);
        intent.putExtra("customerLocation", customerLocation);
        intent.putExtra("id", id);

        startActivity(intent);
    }

    public void makeCall(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+customerPhoneNumber));
        startActivity(intent);
    }
}
