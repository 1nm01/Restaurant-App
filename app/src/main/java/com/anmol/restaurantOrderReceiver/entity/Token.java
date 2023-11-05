package com.anmol.restaurantOrderReceiver.entity;

public class Token {
    private String restId;
    private String token;

    public Token(String restId, String token) {
        this.restId = restId;
        this.token = token;
    }
}
