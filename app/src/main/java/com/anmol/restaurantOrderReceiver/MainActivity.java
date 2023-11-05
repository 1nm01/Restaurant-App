package com.anmol.restaurantOrderReceiver;

import static java.util.Collections.copy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.anmol.restaurantOrderReceiver.adapter.OrderAdapter;
import com.anmol.restaurantOrderReceiver.adapter.OrderRecyclerAdapter;
import com.anmol.restaurantOrderReceiver.dao.OrderDao;
import com.anmol.restaurantOrderReceiver.dbInstance.RestaurantDb;
import com.anmol.restaurantOrderReceiver.entity.Order;
import com.anmol.restaurantOrderReceiver.viewModel.FcmViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    public static Context context;
    private List<Order> orders = new ArrayList<>();
    private List<Order> preparedOrders = new ArrayList<>();
    private List<Order> deliveredOrders = new ArrayList<>();
    private List<Order> pendingOrders = new ArrayList<>();
    private OrderRecyclerAdapter adapter;

    private int state = 0;
    private FcmViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        if(getSharedPreferences("login", MODE_PRIVATE).getString("jwtToken", "").equals("")){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        checkNotificationPermission();
        Log.e("anmol1","hello");
        // Call db from async operation
        RestaurantDb db = Room.databaseBuilder(getApplicationContext(),
                RestaurantDb.class, "restaurant").allowMainThreadQueries().build();
        OrderDao orderDao = db.orderDao();
//        orderDao.insertOrder(new Order("Anmol", "9530", "12","items","Pending"), new Order("Gunjan", "708", "12","items", "Pending"));
        List<Order> allOrders = orderDao.getAllOrders();
        Intent intent = getIntent();
        if(intent.getExtras() != null) {
            state = intent.getExtras().getInt("state");
        }
        if(state == 0) {
            orders = allOrders.stream().filter((element) -> element.getType().equals("Pending")).collect(Collectors.toList());
        }
        else if(state == 1){
            orders = allOrders.stream().filter((element)->element.getType().equals("Prepared")).collect(Collectors.toList());
        }
        else if(state == 2){
            orders = allOrders.stream().filter((element)->element.getType().equals("Delivered")).collect(Collectors.toList());
        }
        pendingOrders = allOrders.stream().filter((element)->element.getType().equals("Pending")).collect(Collectors.toList());
        preparedOrders = allOrders.stream().filter((element)->element.getType().equals("Prepared")).collect(Collectors.toList());
        deliveredOrders = allOrders.stream().filter((element)->element.getType().equals("Delivered")).collect(Collectors.toList());
        RecyclerView recyclerView = findViewById(R.id.recyclerViewOrder);
        ImageView pendingImage = findViewById(R.id.imagePending);
        ImageView preparedImage = findViewById(R.id.imageDone);
        ImageView deliveredImage = findViewById(R.id.imageDelivered);
        ImageView homeImage = findViewById(R.id.imageHome);

//        model = new ViewModelProvider(this).get(FcmViewModel.class);
        FcmViewModel.data.observe(this, new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                Toast.makeText(getBaseContext(), "data changed bacha", Toast.LENGTH_SHORT).show();
                List<Order> newOrder = orderDao.getOrdersWithAllCondition(order.getName(), order.getMobile(), order.getPrice(), order.getItems(), order.getType());
                if(newOrder.size() == 1){
                    pendingOrders.add(newOrder.get(0));
                    if(state == 0){
                        orders.add(newOrder.get(0));
                        adapter.addItems(orders.size()-1);
                    }
                }
            }
        });



        OnRecyclerViewItemClick onRecyclerViewItemClick = new OnRecyclerViewItemClick() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getBaseContext(), "bye" + position, Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                // 2. Chain together various setter methods to set the dialog characteristics.
                builder.setMessage(orders.get(position).getItems())
                        .setTitle("Order for " + orders.get(position).getName() + " is");

                builder.setCancelable(true);
                builder.setPositiveButton("OK", (DialogInterface.OnClickListener) (dialog, which) -> {
                    // When the user click yes button then app will close
                    dialog.cancel();
                });

                // 3. Get the AlertDialog.
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        };

        OnRecyclerViewLongItemClick onRecyclerViewLongItemClick = new OnRecyclerViewLongItemClick() {
            @Override
            public void onLongClick(int position) {
                Toast.makeText(getBaseContext(), "long click" + position, Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

// 2. Chain together various setter methods to set the dialog characteristics.
                builder.setMessage(orders.get(position).getItems())
                        .setTitle("Order for " + orders.get(position).getName() + " is");

                builder.setCancelable(true);
                if(state == 0) {
                    builder.setPositiveButton("Move to Prepared", (DialogInterface.OnClickListener) (dialog, which) -> {
                        // When the user click yes button then app will close
                        preparedOrders.add(orders.remove(position));
                        Order removedOrder = pendingOrders.remove(position);
                        adapter.removeItem(position, orders.size());
                        orderDao.updateType(removedOrder.getId(), "Prepared");
                        dialog.cancel();
                    });
                }

                if(state <=1) {
                    // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
                    builder.setNegativeButton("Move To Delivered", (DialogInterface.OnClickListener) (dialog, which) -> {
                        // If user click no then dialog box is canceled.
                        Order removedOrder = orders.remove(position);
                        deliveredOrders.add(removedOrder);
                        if(state == 0){
                            pendingOrders.remove(position);
                        }
                        else {
                            preparedOrders.remove(position);
                        }
                        adapter.removeItem(position, orders.size());
                        orderDao.updateType(removedOrder.getId(), "Delivered");
                        dialog.cancel();
                    });
                }
                builder.setNeutralButton("Cancel", (DialogInterface.OnClickListener) (dialog, which) -> {
                    // When the user click yes button then app will close
                    dialog.cancel();
                });
// 3. Get the AlertDialog.
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        };
        adapter = new OrderRecyclerAdapter(this, orders, onRecyclerViewItemClick, onRecyclerViewLongItemClick);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        pendingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = 0;
                orders.clear();
                for(Order order: pendingOrders){
                    orders.add(order);
                }
                adapter.notifyDataSetChanged();
            }
        });
        preparedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = 1;
                orders.clear();
                for(Order order: preparedOrders){
                    orders.add(order);
                }
                adapter.notifyDataSetChanged();
            }
        });

        deliveredImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = 2;
                orders.clear();
                for(Order order: deliveredOrders){
                    orders.add(order);
                }
                adapter.notifyDataSetChanged();
            }
        });

        homeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                intent.putExtra("pending",pendingOrders.size());
                intent.putExtra("prepared",preparedOrders.size());
                intent.putExtra("delivered",deliveredOrders.size());
                startActivity(intent);
            }
        });
    }



    private void checkNotificationPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Woho, you have enabled notifications!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Ouch, this is gonna hurt without notifications", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

}