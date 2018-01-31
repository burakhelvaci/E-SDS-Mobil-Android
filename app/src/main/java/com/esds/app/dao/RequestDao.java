package com.esds.app.dao;

import com.esds.app.enums.Request;

import java.util.HashMap;

public interface RequestDao {

    String fetch(final String url, final HashMap<String, String> dataSet, Request request) throws Exception;

    void affect(final String url, final HashMap<String, String> dataSet, Request request) throws Exception;
}
