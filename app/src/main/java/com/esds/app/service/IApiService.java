package com.esds.app.service;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public interface IApiService {
    String prepareLoginData(String username, String password) throws Exception;

    JSONArray prepareDirectionData(final double originLatitude, final double originLongitude, final double destinationLatitude, final double destinationLongitude) throws Exception;

    JSONArray prepareVisitListData(String username) throws Exception;

    void prepareLoggingLocation(String visitId);

    JSONArray prepareCategoriesData() throws Exception;

    JSONArray prepareProductsData(int id) throws Exception;
}
