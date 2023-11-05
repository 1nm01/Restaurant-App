package com.anmol.restaurantOrderReceiver.entity;

public class LoginResponse {
    private int statusCode;
    private String token;
    private String error;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getToken() {
        return token;
    }

    public void setJwtToken(String jwtToken) {
        this.token = jwtToken;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
