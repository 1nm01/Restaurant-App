package com.anmol.restaurantOrderReceiver.viewHolder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anmol.restaurantOrderReceiver.OnRecyclerViewItemClick;
import com.anmol.restaurantOrderReceiver.OnRecyclerViewLongItemClick;
import com.anmol.restaurantOrderReceiver.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private Context context;
    private TextView textViewName, textViewPhone, textViewPrice;
    private int position;
    private OnRecyclerViewItemClick onRecyclerViewItemClick;
    private OnRecyclerViewLongItemClick onRecyclerViewLongItemClick;
    public OrderViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        textViewName = itemView.findViewById(R.id.textViewName);
        textViewPhone = itemView.findViewById(R.id.textViewPhone);
        textViewPrice = itemView.findViewById(R.id.textViewPrice);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "heelo", Toast.LENGTH_SHORT).show();
                onRecyclerViewItemClick.onClick(position);
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onRecyclerViewLongItemClick.onLongClick(position);
                return true;
            }
        });
        this.context = context;
    }

    public void setPosition(int position){
        this.position = position;
    }

    public void setOnRecyclerViewItemClick(OnRecyclerViewItemClick onRecyclerViewItemClick){
        this.onRecyclerViewItemClick = onRecyclerViewItemClick;
    }
    public void setOnRecyclerViewLongItemClick(OnRecyclerViewLongItemClick onRecyclerViewLongItemClick){
        this.onRecyclerViewLongItemClick = onRecyclerViewLongItemClick;
    }
    public TextView getTextViewName() {
        return textViewName;
    }

    public TextView getTextViewPhone() {
        return textViewPhone;
    }

    public TextView getTextViewPrice() {
        return textViewPrice;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public boolean onLongClick(View v) {
//        return true;
//    }
//
//    @Override
//    public boolean onLongClickUseDefaultHapticFeedback(@NonNull View v) {
//        return View.OnLongClickListener.super.onLongClickUseDefaultHapticFeedback(v);
//    }
}
