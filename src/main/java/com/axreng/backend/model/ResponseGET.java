package com.axreng.backend.model;

public class ResponseGET {
    private String id;
    private String status;
    private String[] urls;

    public ResponseGET() {
    }

    public ResponseGET(String id, String status, String[] urls) {
        this.id = id;
        this.status = status;
        this.urls = urls;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
    }

}
