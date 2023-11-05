package com.anmol.restaurantOrderReceiver.restrofitClients;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitLoginClient {
    public static Retrofit getLoginRetrofitInstance(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://mt2hwo1l96.execute-api.ap-south-1.amazonaws.com/test/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

}
