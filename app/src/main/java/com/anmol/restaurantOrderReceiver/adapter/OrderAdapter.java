package com.anmol.restaurantOrderReceiver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anmol.restaurantOrderReceiver.R;
import com.anmol.restaurantOrderReceiver.entity.Order;

import java.util.List;

public class OrderAdapter extends ArrayAdapter<Order> {

    List<Order> orders;
    public OrderAdapter(@NonNull Context context, int resource, @NonNull List<Order> objects) {
        super(context, resource, objects);
        this.orders = objects;
    }

    @Nullable
    @Override
    public Order getItem(int position) {
        return orders.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.order_layout, parent, false);
        TextView textViewName = convertView.findViewById(R.id.textViewName);
        TextView textViewPhone = convertView.findViewById(R.id.textViewPhone);
        TextView textViewPrice = convertView.findViewById(R.id.textViewPrice);
        textViewName.setText(getItem(position).getName());
        textViewPhone.setText(getItem(position).getMobile());
        textViewPrice.setText(getItem(position).getPrice());
        return convertView;
    }
}
