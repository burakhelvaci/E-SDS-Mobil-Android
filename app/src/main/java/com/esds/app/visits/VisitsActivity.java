package com.esds.app.visits;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.esds.app.R;
import com.esds.app.properties.Request;
import com.esds.app.service.RestService;
import com.esds.app.service.impl.RestServiceImpl;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class VisitsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    RestService apiService;

    JSONArray visitArr = new JSONArray();
    BaseAdapter baseAdapter;
    LayoutInflater layoutInflater;
    SwipeRefreshLayout swipeRefreshLayout;
    ListView listView;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visits);

        apiService = new RestServiceImpl();

        userName = getIntent().getExtras().getString("userName");
        listView = findViewById(R.id.visit_listview);
        layoutInflater = LayoutInflater.from(VisitsActivity.this);

        swipeRefreshLayout = findViewById(R.id.srl_visits);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    HashMap<String, String> dataSet = new HashMap<>();
                    dataSet.put("userName", userName);
                    visitArr = apiService.fetch("http://192.168.1.38:8080/getVisitsForMobile", dataSet, Request.POST);
                    baseAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return visitArr.length();
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view == null){
                    view = layoutInflater.inflate(R.layout.item_visits, null);
                }

                try {
                    JSONObject visitItem = visitArr.getJSONObject(i);
                    String customerName = visitItem.getJSONObject("customer").getString("name");
                    String visitDate = visitItem.getString("visitDate");

                    TextView tvName = view.findViewById(R.id.customer_name);
                    tvName.setText(customerName);

                    TextView tvDate = view.findViewById(R.id.visit_date);
                    tvDate.setText(visitDate);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return view;
            }
        };

        listView.setAdapter(baseAdapter);
        listView.setOnItemClickListener(this);

        try {
            HashMap<String, String> dataSet = new HashMap<>();
            dataSet.put("userName", userName);
            visitArr = apiService.fetch("http://192.168.1.38:8080/getVisitsForMobile", dataSet, Request.POST);
            baseAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            JSONObject visitItem = visitArr.getJSONObject(i);
            String customerName = visitItem.getJSONObject("customer").getString("name");
            String customerPhoneNumber = visitItem.getJSONObject("customer").getString("phoneNumber");
            String customerLocation = visitItem.getJSONObject("customer").getString("location");
            String customerAddres = visitItem.getJSONObject("customer").getString("address");
            String visitDate = visitItem.getString("visitDate");
            String id = visitItem.getString("id");

            Intent intent = new Intent(VisitsActivity.this, VisitDetailActivity.class);
            intent.putExtra("customerName", customerName);
            intent.putExtra("customerPhoneNumber", customerPhoneNumber);
            intent.putExtra("customerLocation", customerLocation);
            intent.putExtra("customerAddres", customerAddres);
            intent.putExtra("visitDate", visitDate);
            intent.putExtra("id", id);

            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
