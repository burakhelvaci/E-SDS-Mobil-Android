package com.esds.app.service;

import com.esds.app.properties.Request;

import org.json.JSONArray;

import java.util.HashMap;

public interface RestService {

    String fetchLoginData(final String url, final HashMap<String, String> dataSet, Request request) throws Exception;

    JSONArray fetch(final String url, final HashMap<String, String> dataSet, Request request) throws Exception;

    JSONArray fetchDirectionData(String url, HashMap<String, String> dataSet, Request request) throws Exception;

    void affect(final String url, final HashMap<String, String> dataSet, Request request) throws Exception;
}