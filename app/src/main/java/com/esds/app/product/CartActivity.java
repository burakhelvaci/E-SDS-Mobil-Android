package com.esds.app.product;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.esds.app.R;
import com.esds.app.dao.EsdsDatabase;
import com.esds.app.model.Cart;
import com.esds.app.enums.Request;
import com.esds.app.service.RequestService;
import com.esds.app.service.impl.RequestServiceImpl;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class CartActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private String hostName;

    private RequestService requestService;
    private EsdsDatabase esdsDatabase;

    private BaseAdapter cartBaseAdapter;
    private LayoutInflater cartLayoutInflater;
    private ListView cartListView;

    private Cart[] carts;
    private int rowCount = 0;
    private int visitId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        hostName = getString(R.string.host_name);

        visitId = getIntent().getExtras().getInt("visitId");

        requestService =  new RequestServiceImpl();
        esdsDatabase = Room.databaseBuilder(getApplicationContext(), EsdsDatabase.class, "cart").fallbackToDestructiveMigration().build();

        new AsyncTask<String, String, String>() {
            @Override
            protected void onPostExecute(String s) {
                cartBaseAdapter.notifyDataSetChanged();
            }

            @Override
            protected String doInBackground(String... strings) {
                carts = esdsDatabase.cartDao().select(visitId);
                rowCount = carts.length;

                return null;
            }
        }.execute();
        cartListView = findViewById(R.id.cart_list_view);
        cartLayoutInflater = LayoutInflater.from(CartActivity.this);

        cartBaseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return rowCount;
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
                    view = cartLayoutInflater.inflate(R.layout.item_cart, null);
                }

                Cart cart = carts [i];

                TextView productName = view.findViewById(R.id.product_name_in_item_cart);
                TextView productCount = view.findViewById(R.id.product_count_in_item_cart);
                TextView productPrice = view.findViewById(R.id.product_price_in_item_cart);
                TextView totalPrice = view.findViewById(R.id.product_total_price_in_item_cart);
                ImageView productImg = view.findViewById(R.id.product_image_in_item_cart);

                Picasso.with(CartActivity.this).load(cart.getProductImgLink()).into(productImg);
                productName.setText(cart.getProductName());
                productCount.setText(cart.getProductCount() + "");
                productPrice.setText(cart.getProductPrice() + "");
                String tPrice = cart.getProductPrice() * cart.getProductCount() + "";
                tPrice = tPrice.substring(0, tPrice.indexOf(".")) + tPrice.substring(tPrice.indexOf(".", 2));
                totalPrice.setText(tPrice + " TL");

                return view;
            }
        };

        cartListView.setAdapter(cartBaseAdapter);
        cartListView.setOnItemClickListener(this);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_save_order);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date today = Calendar.getInstance().getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String orderDate = simpleDateFormat.format(today);

                double totalPrice = 0;
                for(Cart cart : carts){
                    totalPrice = totalPrice + cart.getProductPrice()*cart.getProductCount();
                }

                HashMap<String, String> productDataSet = new HashMap<>();
                productDataSet.put("totalPrice", totalPrice + "");
                productDataSet.put("orderDate", orderDate);
                productDataSet.put("visit.id", visitId+"");

                try {
                    requestService.affect(hostName + "/insertOrdersFromMobile", productDataSet, Request.POST);
                    Toast.makeText(CartActivity.this, "Siparişler Kaydedildi", Toast.LENGTH_SHORT).show();

                    new AsyncTask<String, String, String>(){
                        @Override
                        protected void onPostExecute(String s) {
                            cartBaseAdapter.notifyDataSetChanged();
                        }

                        @Override
                        protected String doInBackground(String... strings) {
                            esdsDatabase.cartDao().truncate(visitId);
                            rowCount=0;
                            return null;
                        }
                    }.execute();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
