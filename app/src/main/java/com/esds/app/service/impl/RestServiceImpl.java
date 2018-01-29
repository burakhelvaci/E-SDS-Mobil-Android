package com.esds.app.service.impl;

import com.esds.app.properties.Request;
import com.esds.app.dao.RestDao;
import com.esds.app.dao.impl.RestDaoImpl;
import com.esds.app.service.RestService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class RestServiceImpl implements RestService {

    RestDao restDao = new RestDaoImpl();

    @Override
    public String fetchLoginData(final String url, final HashMap<String, String> dataSet, Request request) throws Exception{
        return restDao.fetch(url, dataSet, request);
    }

    @Override
    public JSONArray fetch(String url, HashMap<String, String> dataSet, Request request) throws Exception {
        return new JSONArray(restDao.fetch(url, dataSet, request));
    }

    @Override
    public JSONArray fetchDirectionData(String url, HashMap<String, String> dataSet, Request request) throws Exception {
        JSONArray steps = new JSONArray();
        JSONObject directionObj = new JSONObject(restDao.fetch(url, dataSet, request));
        steps = directionObj.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
        return steps;
    }

    @Override
    public void affect(final String url, final HashMap<String, String> dataSet, Request request) throws Exception {
        restDao.affect(url, dataSet, request);
    }

}
