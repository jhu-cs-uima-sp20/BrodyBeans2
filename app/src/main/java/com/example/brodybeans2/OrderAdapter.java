package com.example.brodybeans2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {

    private ArrayList<Order> mOrderList;

    public static class OrderHolder extends RecyclerView.ViewHolder {

        public TextView orderNumberView;

        public OrderHolder(View itemView) {
            super(itemView);
            //TODO edit this
            orderNumberView = itemView.findViewById(R.id.order_number);
        }
    }

    public OrderAdapter(ArrayList<Order> orderList) {
        mOrderList = orderList;
    }

    @Override
    public OrderAdapter.OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order, parent, false);
        OrderAdapter.OrderHolder oh = new OrderAdapter.OrderHolder(v);
        return oh;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderHolder holder, int position) {
        Order currItem = mOrderList.get(position);
        holder.orderNumberView.setText(String.valueOf(currItem.getOrderNumber()));
    }

    @Override
    public int getItemCount() { return mOrderList.size(); }
}
