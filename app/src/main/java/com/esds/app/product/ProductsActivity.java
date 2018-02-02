package com.esds.app.product;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.esds.app.R;
import com.esds.app.dao.EsdsDatabase;
import com.esds.app.model.Cart;
import com.esds.app.enums.Request;
import com.esds.app.service.RequestService;
import com.esds.app.service.impl.RequestServiceImpl;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class ProductsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {

    private String hostName;

    private RequestService requestService;
    private EsdsDatabase esdsDatabase;

    private JSONArray categoryJSONArray = new JSONArray();
    private JSONArray productJSONArray = new JSONArray();
    private HashMap<Integer, Cart> cartsElemenets = new HashMap<>();
    private int visitId;

    private BaseAdapter productBaseAdapter;
    private LayoutInflater productLayoutInflater;
    private ListView productListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        hostName = getString(R.string.host_name);

        requestService = new RequestServiceImpl();
        esdsDatabase = Room.databaseBuilder(getApplicationContext(), EsdsDatabase.class, "cart").fallbackToDestructiveMigration().build();
        visitId = getIntent().getExtras().getInt("visitId");

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        Menu sideMenu = navView.getMenu();
        SubMenu menuGroup = sideMenu.addSubMenu("KATEGORILER");
        getCategories(menuGroup);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_open_cart);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductsActivity.this, CartActivity.class);
                intent.putExtra("visitId", visitId);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        try {
            productJSONArray = requestService.fetch(hostName + "/api/product/getproducts", new HashMap<String, String>(), Request.POST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        productListView = findViewById(R.id.product_list_view);
        productLayoutInflater = LayoutInflater.from(ProductsActivity.this);

        productBaseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return productJSONArray.length();
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
                    view = productLayoutInflater.inflate(R.layout.item_product, null);
                }

                try {
                    JSONObject visitItem = productJSONArray.getJSONObject(i);

                    int productId = Integer.parseInt(visitItem.getString("id"));
                    String productName = visitItem.getString("name");
                    double productPrice = visitItem.getDouble("price");
                    String imgLink = visitItem.getString("imgLink");

                    ImageView ProductImageImageView = view.findViewById(R.id.product_image_in_item_product);
                    TextView productNameTextView = view.findViewById(R.id.product_name_in_item_product);
                    TextView productPriceTextView = view.findViewById(R.id.product_price_in_item_product);

                    Picasso.with(ProductsActivity.this).load(imgLink).into(ProductImageImageView);
                    productNameTextView.setText(productName);
                    productPriceTextView.setText(productPrice + " TL");

                    Cart cart = new Cart();
                    cart.setProductId(productId);
                    cart.setProductName(productName);
                    cart.setProductPrice(productPrice);
                    cart.setProductImgLink(imgLink);
                    cartsElemenets.put(cart.getProductId(), cart);

                    view.setId(productId);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return view;
            }
        };

        productListView.setAdapter(productBaseAdapter);
        productListView.setOnItemClickListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.products, menu);
        //menu.add(R.id.category, Menu.NONE, 0, "Item1");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        try {
            HashMap<String, String> dataSet = new HashMap<>();
            dataSet.put("id", id + "");
            productJSONArray = requestService.fetch(hostName + "/api/product/getproductsbycategory", dataSet, Request.POST);
            productBaseAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getCategories(SubMenu menuGroup) {
        try {
            categoryJSONArray = requestService.fetch(hostName + "/api/category/getcategories", new HashMap<String, String>(), Request.POST);
            for (int i = 0; i < categoryJSONArray.length(); i++) {
                JSONObject category = categoryJSONArray.getJSONObject(i);
                CharSequence itemName = category.getString("name");
                int itemId = Integer.parseInt(category.getString("id"));

                menuGroup.add(Menu.NONE, itemId, Menu.NONE, itemName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sepete Ekle");

        View viewDialog = LayoutInflater.from(this).inflate(R.layout.add_cart_dialog, null);
        builder.setView(viewDialog);
        final NumberPicker addCartDialogNumberPicker = (NumberPicker) viewDialog.findViewById(R.id.number_picker_in_add_cart_dialog);
        addCartDialogNumberPicker.setWrapSelectorWheel(true);
        addCartDialogNumberPicker.setMinValue(1);
        addCartDialogNumberPicker.setMaxValue(999);

        builder.setPositiveButton("Ekle", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                int count = addCartDialogNumberPicker.getValue();
                addtoChart(view.getId(), count);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("İptal", null);
        builder.show();
    }

    private void addtoChart(final int id, final int count) {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                Cart cart = cartsElemenets.get(id);
                cart.setProductCount(count);
                cart.setVisitId(visitId);
                esdsDatabase.cartDao().insert(cart);
                return null;
            }
        }.execute();
    }
}
