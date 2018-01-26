package com.esds.app.dao.impl;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.esds.app.HomeActivity;
import com.esds.app.LoginActivity;
import com.esds.app.dao.IApiDao;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ApiDao implements IApiDao {

    @Override
    public String fetchLoginData(final String username, final String password) throws Exception {
        return new AsyncTask<String, String, String>() {
            String responseData = new String();

            @Override
            protected String doInBackground(String... strings) {
                try {
                    responseData = Jsoup.connect("http://192.168.1.38:8080/doLoginWithMobile")
                            .userAgent("Mozilla")
                            .timeout(10000)
                            .data("username", username)
                            .data("password", password)
                            .ignoreContentType(true)
                            .post()
                            .text();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return responseData;
            }
        }.execute().get();
    }

    @Override
    public String fetchVisitsData(final String username) throws Exception {
        return new AsyncTask<String, String, String>() {
            String responseData = new String();

            @Override
            protected String doInBackground(String... strings) {
                try {
                    responseData = Jsoup.connect("http://192.168.1.38:8080/getVisitsForMobile")
                            .data("username", username)
                            .ignoreContentType(true)
                            .post()
                            .text();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return responseData;
            }
        }.execute().get();
    }

    @Override
    public String fetchDirectionData(final double originLatitude, final double originLongitude, final double destinationLatitude, final double destinationLongitude) throws Exception {
        return new AsyncTask<String, String, String>() {
            String responseData = new String();

            @Override
            protected String doInBackground(String... strings) {
                try {
                    responseData = Jsoup.connect("https://maps.googleapis.com/maps/api/directions/json")
                            .data("origin", originLatitude + "," + originLongitude)
                            .data("destination", destinationLatitude + "," + destinationLongitude)
                            .data("key", "AIzaSyAM2aowp1v_SyqM4sI3WT5Z25AU6wnX5IM")
                            .ignoreContentType(true)
                            .get()
                            .text();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return responseData;
            }
        }.execute().get();
    }

    @Override
    public void emitLoggingLocation(final String visitId) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Jsoup.connect("http://192.168.1.38:8080/logVisitForMobile")
                            .data("id", visitId)
                            .post();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    @Override
    public String fetchCategoriesData() throws Exception {
        return new AsyncTask<String, String, String>() {
            String responseData = new String();

            @Override
            protected String doInBackground(String... strings) {
                try {
                    responseData = Jsoup.connect("http://192.168.1.38:8080/getCategoriesForMobile")
                            .ignoreContentType(true)
                            .post()
                            .text();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return responseData;
            }
        }.execute().get();
    }

    @Override
    public String fetchProductsData(final int id) throws Exception {
        return new AsyncTask<String, String, String>() {
            String responseData = new String();

            @Override
            protected String doInBackground(String... strings) {
                try {
                    responseData = Jsoup.connect("http://192.168.1.38:8080/getProductsForMobile")
                            .data("id", id + "")
                            .ignoreContentType(true)
                            .post()
                            .text();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return responseData;
            }
        }.execute().get();
    }
}
