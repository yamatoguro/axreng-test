package com.axreng.backend.model;

public class ResponsePOST {
    private String id;

    public ResponsePOST(String id) {
        this.id = id;
    }

    public ResponsePOST() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
