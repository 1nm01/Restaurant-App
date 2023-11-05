package com.anmol.restaurantOrderReceiver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anmol.restaurantOrderReceiver.entity.Order;
import com.anmol.restaurantOrderReceiver.viewModel.FcmViewModel;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private int numberOfPending = 0;
    private int numberOfPrepared = 0;
    private int numberOfDelivered = 0;
    private TextView pendingTextView;
    private TextView preparedTextView;
    private TextView deliveredTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ImageView pendingImage = findViewById(R.id.imagePending);
        ImageView preparedImage = findViewById(R.id.imageDone);
        ImageView deliveredImage = findViewById(R.id.imageDelivered);

        pendingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("state", 0);
                startActivity(intent);
            }
        });
        preparedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("state", 1);
                startActivity(intent);
            }
        });
        deliveredImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("state", 2);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        if(intent.getExtras() != null) {
            numberOfPending = intent.getExtras().getInt("pending", 0);
            numberOfPrepared = intent.getExtras().getInt("prepared", 0);
            numberOfDelivered = intent.getExtras().getInt("delivered", 0);
        }

        pendingTextView = findViewById(R.id.textView);
        preparedTextView = findViewById(R.id.textView3);
        deliveredTextView = findViewById(R.id.textView4);

        pendingTextView.setText("Pending Orders " + numberOfPending);
        preparedTextView.setText("Prepared Orders " + numberOfPrepared);
        deliveredTextView.setText("Delivered Orders " + numberOfDelivered);

        FcmViewModel.data.observe(this, new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                Toast.makeText(getBaseContext(), "data changed bacha", Toast.LENGTH_SHORT).show();
                numberOfPending += 1;
                pendingTextView.setText("Pending Orders " + numberOfPending);
            }
        });
    }
}