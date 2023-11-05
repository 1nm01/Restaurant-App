package com.anmol.restaurantOrderReceiver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anmol.restaurantOrderReceiver.controller.Controller;

import com.anmol.restaurantOrderReceiver.entity.Credential;
import com.anmol.restaurantOrderReceiver.entity.FcmResponse;
import com.anmol.restaurantOrderReceiver.entity.LoginResponse;
import com.anmol.restaurantOrderReceiver.entity.Token;
import com.anmol.restaurantOrderReceiver.restrofitClients.RetrofitLoginClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText restIdView = findViewById(R.id.restaurantIdText);
        EditText passwordView = findViewById(R.id.passwordText);
        Button button = findViewById(R.id.loginButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String restId = restIdView.getText().toString();
                String password = passwordView.getText().toString();
                if(restId.equals("") || password.equals("")){
                    Toast.makeText(getBaseContext(), "Required field missing", Toast.LENGTH_SHORT).show();
                }
                Controller service = RetrofitLoginClient.getLoginRetrofitInstance().create(Controller.class);
                Call<LoginResponse> loginResponseCall = service.doLogin(new Credential(restId, password));
                loginResponseCall.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        String jwtToken = response.body().getToken();
                        getSharedPreferences("login", MODE_PRIVATE).edit().putString("jwtToken",jwtToken).commit();
                        String fcm_token = getSharedPreferences("_", MODE_PRIVATE).getString("fcm_token", "");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        if(!fcm_token.equals("")){
                            Call<FcmResponse> fcmResponseCall = service.fcmTokenUpdate(new Token(restId, fcm_token));
                            fcmResponseCall.enqueue(new Callback<FcmResponse>() {
                                @Override
                                public void onResponse(Call<FcmResponse> call, Response<FcmResponse> response) {
                                    String status = response.body().getStatus();
                                }

                                @Override
                                public void onFailure(Call<FcmResponse> call, Throwable t) {

                                }
                            });
                        }
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {

                    }
                });
            }
        });
//
//
//        URL url = null;
//        try {
//            url = new URL("https://dummyjson.com/products");
//        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//        }
//        try {
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            int responseCode = urlConnection.getResponseCode();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
    }
}