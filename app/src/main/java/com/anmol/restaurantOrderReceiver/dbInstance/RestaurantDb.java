package com.anmol.restaurantOrderReceiver.dbInstance;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.anmol.restaurantOrderReceiver.dao.OrderDao;
import com.anmol.restaurantOrderReceiver.entity.Order;

@Database(entities = {Order.class}, version = 1)
public abstract class RestaurantDb extends RoomDatabase {
    public abstract OrderDao orderDao();
}
