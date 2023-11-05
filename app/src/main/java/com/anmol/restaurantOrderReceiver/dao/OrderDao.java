package com.anmol.restaurantOrderReceiver.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.anmol.restaurantOrderReceiver.entity.Order;

import java.util.List;

@Dao
public interface OrderDao {

    @Query("SELECT * FROM `order`")
    List<Order> getAllOrders();

    @Insert
    void insertOrder(Order... orders);

    @Query("Update `order` SET type = :type where id=:id")
    void updateType(int id, String type);

    @Query("Select * From `order` where name = :name and mobile = :mobile and  price = :price and items = :items and type = :type")
    List<Order> getOrdersWithAllCondition(String name, String mobile, String price, String items, String type);
}
