package com.esds.app.products;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;

import com.esds.app.R;
import com.esds.app.service.RestService;
import com.esds.app.service.impl.RestServiceImpl;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProductsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {

    RestService apiService;
    JSONArray categories = new JSONArray();
    JSONArray products = new JSONArray();

    BaseAdapter baseAdapter;
    LayoutInflater layoutInflater;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        apiService = new RestServiceImpl();

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        Menu sideMenu = navView.getMenu();
        SubMenu menuGroup = sideMenu.addSubMenu("KATEGORILER");
        getCategories(menuGroup);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        try {
            products = apiService.fetchProductsData(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        listView = findViewById(R.id.product_list);
        layoutInflater = LayoutInflater.from(ProductsActivity.this);

        baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return products.length();
            }

            @Override
            public Object getItem(int i) { return null; }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view == null) {
                    view = layoutInflater.inflate(R.layout.item_product, null);
                }

                try {
                    JSONObject visitItem = products.getJSONObject(i);
                    int productid = Integer.parseInt(visitItem.getString("id"));
                    // String productCatId = visitItem.getString("category_id");
                    String productName = visitItem.getString("name");
                    String productPrice = visitItem.getString("price");
                    String imgLink = visitItem.getString("img_link");

                    ImageView imageView = view.findViewById(R.id.product_img);
                    Picasso.with(ProductsActivity.this).load(imgLink).into(imageView);

                    TextView tvName = view.findViewById(R.id.product_name);
                    tvName.setText(productName);

                    TextView tvPrice = view.findViewById(R.id.product_price);
                    tvPrice.setText(productPrice + " TL");

                    view.setId(productid);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return view;
            }
        };

        listView.setAdapter(baseAdapter);
        listView.setOnItemClickListener(this);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
            products = apiService.fetchProductsData(id);
            baseAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getCategories(SubMenu menuGroup) {
        try {
            categories = apiService.fetchCategoriesData();
            for (int i = 0; i < categories.length(); i++) {
                JSONObject category = categories.getJSONObject(i);
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
        builder.setMessage("Ürün Sepete Eklensin mi?");

        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                addtoChart(view.getId());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Hayır", null);
        builder.show();
    }

    private void addtoChart(int id) {

    }
}
