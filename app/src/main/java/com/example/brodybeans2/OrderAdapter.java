package com.example.brodybeans2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {

    private ArrayList<Order> mOrderList;
    private OnExpandListener mOnExpandListener;

    public static class OrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView orderNumberView;
        public ImageView expandImageView;
        OnExpandListener onExpandListener;

        public OrderHolder(View itemView, OnExpandListener onExpandListener) {
            super(itemView);
            //TODO edit this
            orderNumberView = itemView.findViewById(R.id.order_number);
            expandImageView = itemView.findViewById(R.id.more_info);
            this.onExpandListener = onExpandListener;

            expandImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onExpandListener.onExpandClick(getAdapterPosition());
        }
    }

    public OrderAdapter(ArrayList<Order> orderList, OnExpandListener mOnExpandListener) {
        mOrderList = orderList;
        this.mOnExpandListener = mOnExpandListener;
    }

    @Override
    public OrderAdapter.OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order, parent, false);
        OrderAdapter.OrderHolder oh = new OrderAdapter.OrderHolder(v, mOnExpandListener);
        return oh;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderHolder holder, int position) {
        Order currItem = mOrderList.get(position);
        holder.orderNumberView.setText(String.valueOf(currItem.getOrderNumber()));
    }

    @Override
    public int getItemCount() { return mOrderList.size(); }

    public interface OnExpandListener {
        void onExpandClick(int position);
    }
}
