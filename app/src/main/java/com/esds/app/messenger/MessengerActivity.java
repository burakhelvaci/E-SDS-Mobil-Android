package com.esds.app.messenger;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.util.Arrays;
import java.util.HashMap;

public class MessengerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView messengerListView;
    private LayoutInflater messengerLayoutInflater;
    private BaseAdapter messengerBaseAdapter;
    private SwipeRefreshLayout messengerSwipeRefreshLayout;

    private JSONObject userJSONObject;
    private String hostName;
    private JSONArray jSONArray = new JSONArray();
    private JSONArray messengerJSONArray = new JSONArray();

    private RequestService requestService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        hostName = getString(R.string.host_name);
        try {
            userJSONObject =  new JSONObject(getIntent().getExtras().getString("user"));
        } catch (Exception e) {
            Log.e("Error", "Exception Throwed", e);
        }

        requestService = new RequestServiceImpl();

        messengerListView = findViewById(R.id.messenger_list_view);
        messengerLayoutInflater = LayoutInflater.from(MessengerActivity.this);
        messengerSwipeRefreshLayout = findViewById(R.id.messenger_srl);
        messengerSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    jSONArray = requestService.fetch(hostName + "/api/messenger/getusers", new HashMap<String, String>(), Request.POST);
                    messengerJSONArray = new JSONArray();
                    for (int i = 0; i < jSONArray.length(); i++) {
                        if (!jSONArray.getJSONObject(i).getString("userName").equals(userJSONObject.getString("userName"))) {
                            messengerJSONArray.put(jSONArray.getJSONObject(i));
                        }
                    }
                } catch (Exception e) {
                    Log.e("Error", "Exception Throwed", e);
                } finally {
                    messengerBaseAdapter.notifyDataSetChanged();
                    messengerSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        messengerBaseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return messengerJSONArray.length();
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
                    view = messengerLayoutInflater.inflate(R.layout.item_contact, null);
                }
                try {
                    TextView nameTextView = view.findViewById(R.id.user_name_in_item_contact);
                    String name = messengerJSONArray.getJSONObject(i).getString("name");
                    nameTextView.setText(name);


                } catch (Exception e) {
                    Log.e("Error", "Exception Throwed", e);
                }

                return view;
            }
        };

        messengerListView.setAdapter(messengerBaseAdapter);

        try {
            jSONArray = requestService.fetch(hostName + "/api/messenger/getusers", new HashMap<String, String>(), Request.POST);
            messengerJSONArray = new JSONArray();
            for (int i = 0; i < jSONArray.length(); i++) {
                if (!jSONArray.getJSONObject(i).getString("userName").equals(userJSONObject.getString("userName")))
                    messengerJSONArray.put(jSONArray.getJSONObject(i));
            }
            messengerBaseAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e("Error", "Exception Throwed", e);
        }

        messengerListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            Intent intent = new Intent(MessengerActivity.this, MessageBoxActivity.class);
            intent.putExtra("receiver", messengerJSONArray.getJSONObject(position).toString());
            startActivity(intent);
        } catch (Exception e) {
            Log.e("Error", "Exception Throwed", e);
        }
    }
}
