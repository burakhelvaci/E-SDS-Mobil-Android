package com.esds.app.properties;

public enum Request {
    GET("GET"),
    POST("POST");

    private String url;

    Request(String url) {
        this.url = url;
    }

    public String url() {
        return url;
    }
}
