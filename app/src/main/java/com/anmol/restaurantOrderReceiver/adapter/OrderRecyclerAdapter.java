package com.anmol.restaurantOrderReceiver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anmol.restaurantOrderReceiver.OnRecyclerViewItemClick;
import com.anmol.restaurantOrderReceiver.OnRecyclerViewLongItemClick;
import com.anmol.restaurantOrderReceiver.R;
import com.anmol.restaurantOrderReceiver.entity.Order;
import com.anmol.restaurantOrderReceiver.viewHolder.OrderViewHolder;

import java.util.List;

public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderViewHolder> {

    private Context context;
    private List<Order> orders;

    private OnRecyclerViewItemClick onRecyclerViewItemClick;
    private OnRecyclerViewLongItemClick onRecyclerViewLongItemClick;
    public OrderRecyclerAdapter(Context context, List<Order> orders, OnRecyclerViewItemClick onRecyclerViewItemClick, OnRecyclerViewLongItemClick onRecyclerViewLongItemClick){
        this.context = context;
        this.orders = orders;
        this.onRecyclerViewItemClick = onRecyclerViewItemClick;
        this.onRecyclerViewLongItemClick = onRecyclerViewLongItemClick;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderViewHolder(LayoutInflater.from(context).inflate(R.layout.order_layout, parent, false), context);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.setPosition(position);
        holder.setOnRecyclerViewItemClick(onRecyclerViewItemClick);
        holder.setOnRecyclerViewLongItemClick(onRecyclerViewLongItemClick);
        holder.getTextViewName().setText(orders.get(position).getName());
        holder.getTextViewPhone().setText(orders.get(position).getMobile());
        holder.getTextViewPrice().setText(orders.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void changeDataSet(){
        notifyDataSetChanged();
    }

    public void removeItem(int position, int size){
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, size);
    }

    public void addItems(int position){
        notifyItemInserted(position);
    }

}
