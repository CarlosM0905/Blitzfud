package com.blitzfud.models;

public class ResponseAPI {
    private String message;

    public ResponseAPI() {
    }

    public ResponseAPI(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
