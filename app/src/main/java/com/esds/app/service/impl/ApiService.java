package com.esds.app.service.impl;

import com.esds.app.dao.IApiDao;
import com.esds.app.dao.impl.ApiDao;
import com.esds.app.service.IApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class ApiService implements IApiService {

    IApiDao apiDao = new ApiDao();

    @Override
    public String prepareLoginData(String username, String password) throws Exception {
        return apiDao.fetchLoginData(username, password);
    }

    @Override
    public JSONArray prepareVisitListData(String username) throws Exception {
        JSONArray visits = new JSONArray(apiDao.fetchVisitsData(username));
        return visits;
    }

    @Override
    public JSONArray prepareDirectionData(double originLatitude, double originLongitude, double destinationLatitude, double destinationLongitude) throws Exception {
        JSONArray steps = new JSONArray();
        JSONObject directionObj = new JSONObject(apiDao.fetchDirectionData(originLatitude, originLongitude, destinationLatitude, destinationLongitude));
        steps = directionObj.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
        return steps;
    }

    @Override
    public void prepareLoggingLocation(String visitId) {
        apiDao.emitLoggingLocation(visitId);
    }

    @Override
    public JSONArray prepareCategoriesData() throws Exception {
        JSONArray categories = new JSONArray(apiDao.fetchCategoriesData());
        return categories;
    }

    @Override
    public JSONArray prepareProductsData(int id) throws Exception {
        JSONArray products = new JSONArray(apiDao.fetchProductsData(id));
        return products;
    }
}
