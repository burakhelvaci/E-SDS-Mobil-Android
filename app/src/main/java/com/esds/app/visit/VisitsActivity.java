package com.esds.app.visit;

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
import com.esds.app.enums.Request;
import com.esds.app.service.RequestService;
import com.esds.app.service.impl.RequestServiceImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class VisitsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private String hostName;

    private RequestService requestService;

    private JSONArray visitJSONArray = new JSONArray();
    private String userName;

    private BaseAdapter visitBaseAdapter;
    private LayoutInflater visitLayoutInflater;
    private SwipeRefreshLayout visitSwipeRefreshLayout;
    private ListView visitListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visits);

        hostName = getString(R.string.host_name);

        requestService = new RequestServiceImpl();

        userName = getIntent().getExtras().getString("userName");
        visitListView = findViewById(R.id.visit_list_view);
        visitLayoutInflater = LayoutInflater.from(VisitsActivity.this);
        visitSwipeRefreshLayout = findViewById(R.id.srl_visits);
        visitSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    HashMap<String, String> dataSet = new HashMap<>();
                    dataSet.put("userName", userName);
                    visitJSONArray = requestService.fetch(hostName + "/api/visit/getvisits", dataSet, Request.POST);
                    visitBaseAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    visitSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        visitBaseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return visitJSONArray.length();
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
                if (view == null) {
                    view = visitLayoutInflater.inflate(android.R.layout.simple_list_item_2, null);
                }

                try {
                    JSONObject visitItem = visitJSONArray.getJSONObject(i);
                    String customerName = visitItem.getJSONObject("customer").getString("name");
                    String visitDate = visitItem.getString("visitDate");

                    TextView tvName = view.findViewById(android.R.id.text1);
                    tvName.setText(customerName);

                    TextView tvDate = view.findViewById(android.R.id.text2);
                    tvDate.setText(visitDate);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return view;
            }
        };

        visitListView.setAdapter(visitBaseAdapter);
        visitListView.setOnItemClickListener(VisitsActivity.this);

        try {
            HashMap<String, String> dataSet = new HashMap<>();
            dataSet.put("userName", userName);
            visitJSONArray = requestService.fetch(hostName + "/api/visit/getvisits", dataSet, Request.POST);
            visitBaseAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            JSONObject visitItem = visitJSONArray.getJSONObject(i);
            int visitId = visitItem.getInt("id");
            String visitDate = visitItem.getString("visitDate");
            String customerName = visitItem.getJSONObject("customer").getString("name");
            String customerPhoneNumber = visitItem.getJSONObject("customer").getString("phoneNumber");
            String customerLocation = visitItem.getJSONObject("customer").getString("location");
            String customerAddress = visitItem.getJSONObject("customer").getString("address");


            Intent intent = new Intent(VisitsActivity.this, VisitDetailActivity.class);
            intent.putExtra("visitId", visitId);
            intent.putExtra("visitDate", visitDate);
            intent.putExtra("customerName", customerName);
            intent.putExtra("customerPhoneNumber", customerPhoneNumber);
            intent.putExtra("customerLocation", customerLocation);
            intent.putExtra("customerAddress", customerAddress);

            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
