package com.esds.app.service;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public interface RestService {
    String fetchLoginData(String username, String password) throws Exception;

    JSONArray fetchDirectionData(final double originLatitude, final double originLongitude, final double destinationLatitude, final double destinationLongitude) throws Exception;

    JSONArray fetchVisitsData(String username) throws Exception;

    void setLocationCheck(String visitId);

    JSONArray fetchCategoriesData() throws Exception;

    JSONArray fetchProductsData(int id) throws Exception;
}
