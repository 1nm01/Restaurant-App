package com.anmol.restaurantOrderReceiver.entity;

public class Credential {
    private String restId;
    private String password;

    public Credential(String restId, String password) {
        this.restId = restId;
        this.password = password;
    }

    public String getRestId() {
        return restId;
    }

    public void setRestId(String restId) {
        this.restId = restId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
