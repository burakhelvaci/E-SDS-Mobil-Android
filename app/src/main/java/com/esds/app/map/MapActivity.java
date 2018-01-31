package com.esds.app.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.esds.app.R;
import com.esds.app.enums.Request;
import com.esds.app.service.RequestService;
import com.esds.app.service.impl.RequestServiceImpl;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;

import java.util.HashMap;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    String hostName;

    private GoogleMap map;
    RequestService requestService = new RequestServiceImpl();

    private LocationManager locationManager;
    private boolean isGpsOpen = false;
    private boolean isNetworkOpen = false;

    static final int LOCATION_REQUEST = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        hostName = getString(R.string.host_name);

        map.getUiSettings().setZoomControlsEnabled(true);
        requestService = new RequestServiceImpl();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mapInitializer();
    }

    @SuppressLint("MissingPermission")
    private void mapInitializer(){
        String visitId = getIntent().getExtras().getString("id");
        String customerName = getIntent().getExtras().getString("customerName");
        String[] customerLocation = getIntent().getExtras().getString("customerLocation").split(",");

        double destinationLat = Double.parseDouble(customerLocation[0]);
        double destinationLng = Double.parseDouble(customerLocation[1]);

        showMyLocation();

        addMarker("Müşteri Lokasyonu", customerName, destinationLat, destinationLng);

        Location location = initLocationListener(visitId, destinationLat, destinationLng);
        if (location != null) {
            addMarker("Başlangıç Lokasyonu", customerName, location.getLatitude(), location.getLongitude());
            drawDirection(location.getLatitude(), location.getLongitude(), destinationLat, destinationLng);
        } else {
            map.setMyLocationEnabled(false);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mapInitializer();
                } else {
                    map.setMyLocationEnabled(false);
                }
                break;
        }
    }

    private void showMyLocation() {
        isGpsOpen = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        isNetworkOpen = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isGpsOpen && isNetworkOpen) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST);
                return;
            }
            map.setMyLocationEnabled(true);
        }
    }

    private Location initLocationListener(final String visitId, final Double destinationLat, final Double destinationLng) {
        final LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                Location destination = new Location(LocationManager.NETWORK_PROVIDER);
                destination.setLatitude(destinationLat);
                destination.setLongitude(destinationLng);

                if (location.distanceTo(destination) < 100) {
                    HashMap<String, String> dataSet = new HashMap<>();
                    dataSet.put("id", visitId);
                    try {
                        requestService.affect(hostName + "/logVisitForMobile", dataSet, Request.POST);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 100, locationListener);
        return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    }

    private void drawDirection(final double originLat, final double originLng, final double destinationLat, final double destinationLng) {
        try {
            HashMap<String, String> dataSet = new HashMap<>();
            dataSet.put("origin", originLat + "," + originLng);
            dataSet.put("destination", destinationLat + "," + destinationLng);
            dataSet.put("key", getString(R.string.google_maps_key));
            JSONArray steps = requestService.fetchDirectionData("https://maps.googleapis.com/maps/api/directions/json", dataSet,Request.GET);
            for (int i = 0; i < steps.length(); i++) {
                double startLat = Double.parseDouble(steps.getJSONObject(i).getJSONObject("start_location").get("lat").toString());
                double startLng = Double.parseDouble(steps.getJSONObject(i).getJSONObject("start_location").get("lng").toString());
                double endLat = Double.parseDouble(steps.getJSONObject(i).getJSONObject("end_location").get("lat").toString());
                double endLng = Double.parseDouble(steps.getJSONObject(i).getJSONObject("end_location").get("lng").toString());

                map.addPolyline(new PolylineOptions().add(new LatLng(startLat, startLng), new LatLng(endLat, endLng)).width(8).color(Color.GREEN));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addMarker(String title, String snippet, Double lat, Double lng) {
        MarkerOptions marker = new MarkerOptions();
        marker.title(title);
        marker.snippet(snippet);
        LatLng latLngDestination = new LatLng(lat, lng);
        marker.position(latLngDestination);
        map.addMarker(marker);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngDestination, 11f));
    }
}