package com.axreng.backend.model;

public class ResponseERROR {
    private int status;
    private String message;

    public ResponseERROR() {
    }

    public ResponseERROR(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
