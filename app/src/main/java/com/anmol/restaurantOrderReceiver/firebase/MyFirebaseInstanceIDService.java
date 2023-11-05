package com.anmol.restaurantOrderReceiver.firebase;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
//import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.room.Room;

import com.anmol.restaurantOrderReceiver.MainActivity;
import com.anmol.restaurantOrderReceiver.R;
import com.anmol.restaurantOrderReceiver.dao.OrderDao;
import com.anmol.restaurantOrderReceiver.dbInstance.RestaurantDb;
import com.anmol.restaurantOrderReceiver.entity.Order;
import com.anmol.restaurantOrderReceiver.viewModel.FcmViewModel;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {
    private String CHANNEL_ID = "i.apps.notifications";
    private String channel_description = "Test notification";
    private String channel_name = "channel1";

//    private FcmViewModel model;

    //        orderDao.insertOrder(new Order("Anmol", "9530", "12","items","Pending"), new Order("Gunjan", "708", "12","items", "Pending"));
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = channel_name;
            String description = channel_description;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private NotificationCompat.Builder getBuilder(String messageTitle, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that fires when the user taps the notification.
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        return builder;
    }

    @SuppressLint("WrongThread")
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);


       RestaurantDb db = Room.databaseBuilder(this,
                RestaurantDb.class, "restaurant").build();
        OrderDao orderDao = db.orderDao();
//        getSharedPreferences("order", MODE_PRIVATE).edit().putString(message.getNotification().getTitle(), message.getNotification().getBody()).apply();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        String[] title = message.getData().get("title").split(";");
//        model = ViewModelProviders.of(this).get(FcmViewModel.class);
        Order newOrder = new Order(title[0], title[1], title[2], message.getData().get("body"), "Pending");
//        model.changeLiveData(newOrder);
        orderDao.insertOrder(newOrder);
        FcmViewModel.data.postValue(newOrder);
        createNotificationChannel();
        NotificationCompat.Builder builder = getBuilder("New Order", "From Mr/Mrs "+ title[0] +" of Rs "+title[2]);
        notificationManager.notify(1, builder.build());
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.e("fcm_token", token);
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fcm_token", token).apply();
    }

}
