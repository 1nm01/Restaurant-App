package com.anmol.restaurantOrderReceiver.controller;

import com.anmol.restaurantOrderReceiver.entity.Credential;
import com.anmol.restaurantOrderReceiver.entity.FcmResponse;
import com.anmol.restaurantOrderReceiver.entity.LoginResponse;
import com.anmol.restaurantOrderReceiver.entity.Token;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Controller {
    @POST("login")
    Call<LoginResponse> doLogin(@Body Credential credential);

    @POST("fcmtoken")
    Call<FcmResponse> fcmTokenUpdate(@Body Token token);
}
