package com.esds.app.service.impl;

import android.util.Log;

import com.esds.app.enums.Request;
import com.esds.app.dao.RequestDao;
import com.esds.app.dao.impl.RequestDaoImpl;
import com.esds.app.service.RequestService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class RequestServiceImpl implements RequestService {

    RequestDao requestDao = new RequestDaoImpl();

    @Override
    public String fetchLoginData(final String url, final HashMap<String, String> dataSet, Request request) throws Exception{
        return requestDao.fetch(url, dataSet, request);
    }

    @Override
    public JSONArray fetch(String url, HashMap<String, String> dataSet, Request request) throws Exception {
        return new JSONArray(requestDao.fetch(url, dataSet, request));
    }

    @Override
    public JSONArray fetchDirectionData(String url, HashMap<String, String> dataSet, Request request) throws Exception {
        JSONArray steps = new JSONArray();
        JSONObject directionObj = new JSONObject(requestDao.fetch(url, dataSet, request));
        steps = directionObj.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
        return steps;
    }

    @Override
    public void affect(final String url, final HashMap<String, String> dataSet, Request request) throws Exception {
        requestDao.affect(url, dataSet, request);
    }

}
