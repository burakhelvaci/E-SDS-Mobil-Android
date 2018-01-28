package com.esds.app.dao;

import org.json.JSONArray;

import java.util.concurrent.ExecutionException;

public interface RestDao {

    String fetchLoginData(final String username, final String password) throws Exception;

    String fetchDirectionData(final double originLat, final double originLng, final double destinationLat, final double destinationLng) throws Exception;

    String fetchVisitsData(final String username) throws Exception;

    String fetchCategoriesData() throws Exception;

    String fetchProductsData(int id) throws Exception;

    void setLocationCheck(String visitId);
}
