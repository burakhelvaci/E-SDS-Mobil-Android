package com.esds.app.visit;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.esds.app.R;
import com.esds.app.map.MapActivity;
import com.esds.app.product.ProductsActivity;

public class VisitDetailActivity extends AppCompatActivity {

    private int visitId;
    private String visitDate;
    private String customerName;
    private String customerPhoneNumber;
    private String customerLocation;
    private String customerAddress;

    private TextView customerNameTextView;
    private TextView customerPhoneNumberTextView;
    private TextView customerAddressTextView;
    private TextView vistDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_detail);

        visitId = getIntent().getExtras().getInt("visitId");

        visitDate = getIntent().getExtras().getString("visitDate");
        customerName = getIntent().getExtras().getString("customerName");
        customerPhoneNumber = getIntent().getExtras().getString("customerPhoneNumber");
        customerLocation = getIntent().getExtras().getString("customerLocation");
        customerAddress = getIntent().getExtras().getString("customerAddress");

        customerNameTextView = findViewById(R.id.customer_name_in_visit_detail);
        customerPhoneNumberTextView = findViewById(R.id.customer_phone_number_in_visit_detail);
        customerAddressTextView = findViewById(R.id.customer_address_in_visit_detail);
        vistDateTextView = findViewById(R.id.visit_date_in_visit_detail);

        customerNameTextView.setText(customerName);
        customerPhoneNumberTextView.setText(customerPhoneNumber);
        customerAddressTextView.setText(customerAddress);
        vistDateTextView.setText(visitDate);
    }

    public void getLocation(View view) {
        Intent intent = new Intent(VisitDetailActivity.this, MapActivity.class);
        intent.putExtra("customerName", customerName);
        intent.putExtra("customerLocation", customerLocation);
        intent.putExtra("id", visitId);

        startActivity(intent);
    }

    public void makeCall(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+customerPhoneNumber));
        startActivity(intent);
    }

    public void giveOrder(View view) {
        Intent intent = new Intent(VisitDetailActivity.this, ProductsActivity.class);
        intent.putExtra("visitId", visitId);
        startActivity(intent);
    }
}
