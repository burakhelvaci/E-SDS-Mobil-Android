package com.esds.app.dao.impl;

import android.os.AsyncTask;
import android.util.Log;

import com.esds.app.dao.RequestDao;
import com.esds.app.enums.Request;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.HashMap;
import java.util.Iterator;

public class RequestDaoImpl implements RequestDao {

    @Override
    public String fetch(final String url, final HashMap<String, String> dataSet, final Request request) throws Exception {
        return new AsyncTask<String, String, String>() {
            String responseData = new String();
            Iterator<String> iterator = dataSet.keySet().iterator();

            @Override
            protected String doInBackground(String... strings) {
                try {
                    Connection connection = Jsoup.connect(url).timeout(1000);
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        String value = dataSet.get(key);
                        connection = jsoupHelper(connection, key, value);
                    }
                    if (request.equals(Request.GET))
                        responseData = connection.ignoreContentType(true).get().text();
                    else if (request.equals(Request.POST))
                        responseData = connection.ignoreContentType(true).post().text();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return responseData;
            }
        }.execute().get();
    }

    @Override
    public void affect(final String url, final HashMap<String, String> dataSet, final Request request) throws Exception {
        new AsyncTask<String, String, String>() {
            Iterator<String> iterator = dataSet.keySet().iterator();

            @Override
            protected String doInBackground(String... strings) {
                try {
                    Connection connection = Jsoup.connect(url).timeout(1000).ignoreContentType(true);
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        String value = dataSet.get(key);
                        connection = jsoupHelper(connection, key, value);
                    }

                    if(request.equals(Request.GET))
                        connection.get();
                    else if (request.equals(Request.POST))
                        connection.post();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    private Connection jsoupHelper(Connection connection, String key, String value) {
        connection = connection.data(key, value);
        return connection;
    }

}
