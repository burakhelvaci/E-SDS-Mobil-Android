package com.esds.app.service.impl;

import com.esds.app.dao.RestDao;
import com.esds.app.dao.impl.RestDaoImpl;
import com.esds.app.service.RestService;

import org.json.JSONArray;
import org.json.JSONObject;

public class RestServiceImpl implements RestService {

    RestDao restDao = new RestDaoImpl();

    @Override
    public String fetchLoginData(String username, String password) throws Exception {
        return restDao.fetchLoginData(username, password);
    }

    @Override
    public JSONArray fetchVisitsData(String username) throws Exception {
        JSONArray visits = new JSONArray(restDao.fetchVisitsData(username));
        return visits;
    }

    @Override
    public JSONArray fetchDirectionData(double originLatitude, double originLongitude, double destinationLatitude, double destinationLongitude) throws Exception {
        JSONArray steps = new JSONArray();
        JSONObject directionObj = new JSONObject(restDao.fetchDirectionData(originLatitude, originLongitude, destinationLatitude, destinationLongitude));
        steps = directionObj.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
        return steps;
    }

    @Override
    public JSONArray fetchCategoriesData() throws Exception {
        JSONArray categories = new JSONArray(restDao.fetchCategoriesData());
        return categories;
    }

    @Override
    public JSONArray fetchProductsData(int id) throws Exception {
        JSONArray products = new JSONArray(restDao.fetchProductsData(id));
        return products;
    }

    @Override
    public void setLocationCheck(String visitId) {
        restDao.setLocationCheck(visitId);
    }

}
