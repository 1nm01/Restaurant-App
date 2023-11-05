package com.anmol.restaurantOrderReceiver.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "order")
public class Order {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "mobile")
    private String mobile;
    @ColumnInfo(name = "price")
    private String price;
    @ColumnInfo(name = "items")
    private String items;
    @ColumnInfo(name="type")
    @NonNull
    private String type;

    public Order() {
    }

    public Order(String name, String mobile, String price, String items, String type) {
        this.name = name;
        this.mobile = mobile;
        this.price = price;
        this.items = items;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
