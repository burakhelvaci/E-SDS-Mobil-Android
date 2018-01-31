package com.esds.app.enums;

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
